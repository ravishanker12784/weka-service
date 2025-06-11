package com.example.recommendation.repository;

import com.example.recommendation.entity.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface RatingRepository extends JpaRepository<Rating, Long> {
    List<Rating> findByUserId(Long userId);
    List<Rating> findByItemId(Long itemId);
    
    @Query("SELECT r FROM Rating r WHERE r.user.id = ?1 AND r.item.id = ?2")
    Rating findByUserIdAndItemId(Long userId, Long itemId);
}
