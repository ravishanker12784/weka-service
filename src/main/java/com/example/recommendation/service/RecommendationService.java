package com.example.recommendation.service;

import com.example.recommendation.entity.Item;
import com.example.recommendation.entity.Rating;
import com.example.recommendation.entity.User;
import com.example.recommendation.repository.ItemRepository;
import com.example.recommendation.repository.RatingRepository;
import com.example.recommendation.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.ReplaceMissingValues;
import weka.classifiers.functions.LinearRegression;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RecommendationService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private ItemRepository itemRepository;
    
    @Autowired
    private RatingRepository ratingRepository;
    
    public List<Item> getRecommendations(String username, int limit) {
        try {
            User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
            
            // Get all ratings data
            List<Rating> allRatings = ratingRepository.findAll();
            List<User> allUsers = userRepository.findAll();
            List<Item> allItems = itemRepository.findAll();
            
            // Create user-item matrix using Weka
            Instances dataset = createUserItemMatrix(allUsers, allItems, allRatings);
            
            // Train collaborative filtering model
            LinearRegression model = new LinearRegression();
            model.buildClassifier(dataset);
            
            // Generate predictions for unrated items
            List<Item> recommendations = generateRecommendations(user, allItems, allRatings, model, dataset);
            
            return recommendations.stream().limit(limit).collect(Collectors.toList());
            
        } catch (Exception e) {
            throw new RuntimeException("Error generating recommendations", e);
        }
    }
    
    private Instances createUserItemMatrix(List<User> users, List<Item> items, List<Rating> ratings) {
        // Create attributes for each item + rating attribute
        ArrayList<Attribute> attributes = new ArrayList<>();
        
        // Add item attributes (binary: user rated this item or not)
        for (Item item : items) {
            attributes.add(new Attribute("item_" + item.getId()));
        }
        
        // Add rating attribute (target)
        attributes.add(new Attribute("rating"));
        
        // Create dataset
        Instances dataset = new Instances("UserItemMatrix", attributes, 0);
        dataset.setClassIndex(dataset.numAttributes() - 1);
        
        // Create rating map for quick lookup
        Map<String, Double> ratingMap = new HashMap<>();
        for (Rating rating : ratings) {
            String key = rating.getUser().getId() + "_" + rating.getItem().getId();
            ratingMap.put(key, rating.getRating());
        }
        
        // Create instances for each user-item combination that has a rating
        for (Rating rating : ratings) {
            Instance instance = new DenseInstance(attributes.size());
            instance.setDataset(dataset);
            
            // Set item features (1 for items the user has rated, 0 otherwise)
            for (int i = 0; i < items.size(); i++) {
                String key = rating.getUser().getId() + "_" + items.get(i).getId();
                instance.setValue(i, ratingMap.containsKey(key) ? 1.0 : 0.0);
            }
            
            // Set target rating
            instance.setValue(attributes.size() - 1, rating.getRating());
            
            dataset.add(instance);
        }
        
        try {
            // Handle missing values
            ReplaceMissingValues replaceMissing = new ReplaceMissingValues();
            replaceMissing.setInputFormat(dataset);
            dataset = Filter.useFilter(dataset, replaceMissing);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return dataset;
    }
    
    private List<Item> generateRecommendations(User user, List<Item> allItems, 
                                             List<Rating> allRatings, 
                                             LinearRegression model, 
                                             Instances dataset) throws Exception {
        
        // Get items already rated by user
        Set<Long> ratedItemIds = allRatings.stream()
            .filter(r -> r.getUser().getId().equals(user.getId()))
            .map(r -> r.getItem().getId())
            .collect(Collectors.toSet());
        
        // Create rating map for the user
        Map<String, Double> userRatings = new HashMap<>();
        for (Rating rating : allRatings) {
            if (rating.getUser().getId().equals(user.getId())) {
                String key = rating.getUser().getId() + "_" + rating.getItem().getId();
                userRatings.put(key, rating.getRating());
            }
        }
        
        List<ItemPrediction> predictions = new ArrayList<>();
        
        // Generate predictions for unrated items
        for (Item item : allItems) {
            if (!ratedItemIds.contains(item.getId())) {
                // Create instance for prediction
                Instance instance = new DenseInstance(dataset.numAttributes());
                instance.setDataset(dataset);
                
                // Set item features based on user's rating history
                for (int i = 0; i < allItems.size(); i++) {
                    String key = user.getId() + "_" + allItems.get(i).getId();
                    instance.setValue(i, userRatings.containsKey(key) ? 1.0 : 0.0);
                }
                
                // Predict rating
                double predictedRating = model.classifyInstance(instance);
                predictions.add(new ItemPrediction(item, predictedRating));
            }
        }
        
        // Sort by predicted rating (descending)
        predictions.sort((a, b) -> Double.compare(b.predictedRating, a.predictedRating));
        
        return predictions.stream()
            .map(p -> p.item)
            .collect(Collectors.toList());
    }
    
    // Helper class for predictions
    private static class ItemPrediction {
        Item item;
        double predictedRating;
        
        ItemPrediction(Item item, double predictedRating) {
            this.item = item;
            this.predictedRating = predictedRating;
        }
    }
    
    // Content-based recommendations using item similarity
    public List<Item> getContentBasedRecommendations(String username, int limit) {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("User not found"));
        
        List<Rating> userRatings = ratingRepository.findByUserId(user.getId());
        
        if (userRatings.isEmpty()) {
            // Return popular items for new users
            return itemRepository.findAll().stream().limit(limit).collect(Collectors.toList());
        }
        
        // Get highly rated items by the user
        List<Item> likedItems = userRatings.stream()
            .filter(r -> r.getRating() >= 4.0)
            .map(Rating::getItem)
            .collect(Collectors.toList());
        
        // Find similar items based on category
        Set<Item> recommendations = new HashSet<>();
        for (Item likedItem : likedItems) {
            List<Item> similarItems = itemRepository.findByCategory(likedItem.getCategory());
            recommendations.addAll(similarItems);
        }
        
        // Remove already rated items
        Set<Long> ratedItemIds = userRatings.stream()
            .map(r -> r.getItem().getId())
            .collect(Collectors.toSet());
        
        return recommendations.stream()
            .filter(item -> !ratedItemIds.contains(item.getId()))
            .limit(limit)
            .collect(Collectors.toList());
    }
}
