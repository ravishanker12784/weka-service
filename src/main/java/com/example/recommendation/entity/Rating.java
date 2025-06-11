// Rating Entity
package com.example.recommendation.entity;

import javax.persistence.*;

@Entity
@Table(name = "ratings")
public class Rating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    
    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;
    
    private Double rating;
    
    // Constructors
    public Rating() {}
    
    public Rating(User user, Item item, Double rating) {
        this.user = user;
        this.item = item;
        this.rating = rating;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    
    public Item getItem() { return item; }
    public void setItem(Item item) { this.item = item; }
    
    public Double getRating() { return rating; }
    public void setRating(Double rating) { this.rating = rating; }
}