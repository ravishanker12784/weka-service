import com.example.recommendation.entity.Item;
import com.example.recommendation.entity.Rating;
import com.example.recommendation.entity.User;
import com.example.recommendation.repository.ItemRepository;
import com.example.recommendation.repository.RatingRepository;
import com.example.recommendation.repository.UserRepository;
import com.example.recommendation.service.RecommendationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.util.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import com.fasterxml.jackson.databind.ObjectMapper;

package com.example.recommendation.controller;





@WebMvcTest(RecommendationController.class)
public class RecommendationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RecommendationService recommendationService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private ItemRepository itemRepository;

    @MockBean
    private RatingRepository ratingRepository;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    void testGetRecommendations_Collaborative() throws Exception {
        String username = "testuser";
        List<Item> items = Arrays.asList(new Item("item1", "cat1", "desc1"));
        when(recommendationService.getRecommendations(username, 10)).thenReturn(items);

        mockMvc.perform(get("/api/recommendations/{username}", username)
                .param("limit", "10")
                .param("type", "collaborative"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("item1"));
    }

    @Test
    void testGetRecommendations_Content() throws Exception {
        String username = "testuser";
        List<Item> items = Arrays.asList(new Item("item2", "cat2", "desc2"));
        when(recommendationService.getContentBasedRecommendations(username, 5)).thenReturn(items);

        mockMvc.perform(get("/api/recommendations/{username}", username)
                .param("limit", "5")
                .param("type", "content"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("item2"));
    }

    @Test
    void testCreateUser() throws Exception {
        User user = new User("newuser");
        user.setId(1L);
        Map<String, String> req = new HashMap<>();
        req.put("username", "newuser");
        when(userRepository.save(ArgumentMatchers.any(User.class))).thenReturn(user);

        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("newuser"));
    }

    @Test
    void testCreateItem() throws Exception {
        Item item = new Item("item3", "cat3", "desc3");
        item.setId(2L);
        Map<String, String> req = new HashMap<>();
        req.put("name", "item3");
        req.put("category", "cat3");
        req.put("description", "desc3");
        when(itemRepository.save(ArgumentMatchers.any(Item.class))).thenReturn(item);

        mockMvc.perform(post("/api/items")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("item3"));
    }

    @Test
    void testAddRating() throws Exception {
        User user = new User("user1");
        user.setId(1L);
        Item item = new Item("item4", "cat4", "desc4");
        item.setId(2L);
        Rating rating = new Rating(user, item, 4.5);
        rating.setId(3L);

        Map<String, Object> req = new HashMap<>();
        req.put("userId", 1L);
        req.put("itemId", 2L);
        req.put("rating", 4.5);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(itemRepository.findById(2L)).thenReturn(Optional.of(item));
        when(ratingRepository.save(ArgumentMatchers.any(Rating.class))).thenReturn(rating);

        mockMvc.perform(post("/api/ratings")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rating").value(4.5));
    }

    @Test
    void testGetAllUsers() throws Exception {
        List<User> users = Arrays.asList(new User("userA"), new User("userB"));
        when(userRepository.findAll()).thenReturn(users);

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].username").value("userA"));
    }

    @Test
    void testGetAllItems() throws Exception {
        List<Item> items = Arrays.asList(new Item("itemA", "catA", "descA"));
        when(itemRepository.findAll()).thenReturn(items);

        mockMvc.perform(get("/api/items"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("itemA"));
    }
}