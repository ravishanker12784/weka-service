package com.example.recommendation.config;

import com.example.recommendation.entity.Item;
import com.example.recommendation.entity.Rating;
import com.example.recommendation.entity.User;
import com.example.recommendation.repository.ItemRepository;
import com.example.recommendation.repository.RatingRepository;
import com.example.recommendation.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private ItemRepository itemRepository;
    
    @Autowired
    private RatingRepository ratingRepository;
    
    @Override
    public void run(String... args) throws Exception {
        // Create sample users
        User user1 = userRepository.save(new User("alice"));
        User user2 = userRepository.save(new User("bob"));
        User user3 = userRepository.save(new User("charlie"));
        
        // Create sample items
        Item item1 = itemRepository.save(new Item("The Matrix", "Sci-Fi", "Cyberpunk movie"));
        Item item2 = itemRepository.save(new Item("Inception", "Sci-Fi", "Dream heist movie"));
        Item item3 = itemRepository.save(new Item("The Shawshank Redemption", "Drama", "Prison drama"));
        Item item4 = itemRepository.save(new Item("Pulp Fiction", "Crime", "Quentin Tarantino film"));
        Item item5 = itemRepository.save(new Item("Interstellar", "Sci-Fi", "Space exploration"));
        
        // Create sample ratings
        ratingRepository.save(new Rating(user1, item1, 5.0));
        ratingRepository.save(new Rating(user1, item2, 4.5));
        ratingRepository.save(new Rating(user1, item3, 4.0));
        
        ratingRepository.save(new Rating(user2, item1, 4.0));
        ratingRepository.save(new Rating(user2, item4, 5.0));
        ratingRepository.save(new Rating(user2, item5, 4.5));
        
        ratingRepository.save(new Rating(user3, item2, 5.0));
        ratingRepository.save(new Rating(user3, item3, 4.5));
        ratingRepository.save(new Rating(user3, item5, 4.0));
        
        System.out.println("Sample data initialized!");
    }
}