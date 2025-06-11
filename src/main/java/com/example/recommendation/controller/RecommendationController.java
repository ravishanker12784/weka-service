package com.example.recommendation.controller;

import com.example.recommendation.entity.Item;
import com.example.recommendation.entity.Rating;
import com.example.recommendation.entity.User;
import com.example.recommendation.repository.ItemRepository;
import com.example.recommendation.repository.RatingRepository;
import com.example.recommendation.repository.UserRepository;
import com.example.recommendation.service.RecommendationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class RecommendationController {
    
    @Autowired
    private RecommendationService recommendationService;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private ItemRepository itemRepository;
    
    @Autowired
    private RatingRepository ratingRepository;
    
    @GetMapping("/recommendations/{username}")
    public ResponseEntity<List<Item>> getRecommendations(
            @PathVariable String username,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "collaborative") String type) {
        
        try {
            List<Item> recommendations;
            if ("content".equals(type)) {
                recommendations = recommendationService.getContentBasedRecommendations(username, limit);
            } else {
                recommendations = recommendationService.getRecommendations(username, limit);
            }
            return ResponseEntity.ok(recommendations);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PostMapping("/users")
    public ResponseEntity<User> createUser(@RequestBody Map<String, String> request) {
        User user = new User(request.get("username"));
        User saved = userRepository.save(user);
        return ResponseEntity.ok(saved);
    }
    
    @PostMapping("/items")
    public ResponseEntity<Item> createItem(@RequestBody Map<String, String> request) {
        Item item = new Item(
            request.get("name"),
            request.get("category"),
            request.get("description")
        );
        Item saved = itemRepository.save(item);
        return ResponseEntity.ok(saved);
    }
    
    @PostMapping("/ratings")
    public ResponseEntity<Rating> addRating(@RequestBody Map<String, Object> request) {
        try {
            Long userId = Long.valueOf(request.get("userId").toString());
            Long itemId = Long.valueOf(request.get("itemId").toString());
            Double rating = Double.valueOf(request.get("rating").toString());
            
            User user = userRepository.findById(userId).orElseThrow();
            Item item = itemRepository.findById(itemId).orElseThrow();
            
            Rating newRating = new Rating(user, item, rating);
            Rating saved = ratingRepository.save(newRating);
            return ResponseEntity.ok(saved);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userRepository.findAll());
    }
    
    @GetMapping("/items")
    public ResponseEntity<List<Item>> getAllItems() {
        return ResponseEntity.ok(itemRepository.findAll());
    }
}