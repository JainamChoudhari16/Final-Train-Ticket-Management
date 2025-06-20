package com.trainmanagement.service;

import com.trainmanagement.model.Food;
import com.trainmanagement.model.dto.FoodDto;
import com.trainmanagement.repository.FoodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class FoodService {

    @Autowired
    private FoodRepository foodRepository;

    public Object getFoodCart() {
        // Return all available food items
        List<Food> availableFood = foodRepository.findByAvailable(true);
        
        Map<String, Object> foodCart = new HashMap<>();
        foodCart.put("availableFood", availableFood);
        foodCart.put("totalItems", availableFood.size());
        
        return foodCart;
    }

    public Object getAllFoodItems() {
        return foodRepository.findAll();
    }

    public Object createFoodItem(FoodDto foodDto) {
        // Validate and parse category
        Food.FoodCategory category;
        try {
            category = Food.FoodCategory.valueOf(foodDto.getCategory().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid food category. Use: BREAKFAST, LUNCH, DINNER, SNACKS, BEVERAGES");
        }
        
        // Create new food item
        Food newFood = new Food(foodDto.getName(), foodDto.getDescription(), 
                foodDto.getPrice(), foodDto.getAvailable(), category);
        Food savedFood = foodRepository.save(newFood);
        
        return savedFood;
    }

    public Object getFoodItemById(Long foodId) {
        Optional<Food> foodOpt = foodRepository.findById(foodId);
        
        if (foodOpt.isEmpty()) {
            throw new RuntimeException("Food item not found");
        }
        
        return foodOpt.get();
    }

    public Object updateFoodItem(Long foodId, FoodDto foodDto) {
        Optional<Food> foodOpt = foodRepository.findById(foodId);
        
        if (foodOpt.isEmpty()) {
            throw new RuntimeException("Food item not found");
        }
        
        Food food = foodOpt.get();
        
        // Update fields
        food.setName(foodDto.getName());
        food.setDescription(foodDto.getDescription());
        food.setPrice(foodDto.getPrice());
        food.setAvailable(foodDto.getAvailable());
        
        // Validate and update category
        Food.FoodCategory category;
        try {
            category = Food.FoodCategory.valueOf(foodDto.getCategory().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid food category. Use: BREAKFAST, LUNCH, DINNER, SNACKS, BEVERAGES");
        }
        food.setCategory(category);
        
        Food updatedFood = foodRepository.save(food);
        return updatedFood;
    }

    public String deleteFoodItem(Long foodId) {
        Optional<Food> foodOpt = foodRepository.findById(foodId);
        
        if (foodOpt.isEmpty()) {
            throw new RuntimeException("Food item not found");
        }
        
        foodRepository.deleteById(foodId);
        return "Food item deleted successfully";
    }

    public Object getFoodItemsByCategory(String category) {
        try {
            Food.FoodCategory foodCategory = Food.FoodCategory.valueOf(category.toUpperCase());
            return foodRepository.findByCategoryAndAvailable(foodCategory, true);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid food category. Use: BREAKFAST, LUNCH, DINNER, SNACKS, BEVERAGES");
        }
    }

    public Object getAvailableFoodItems() {
        return foodRepository.findByAvailable(true);
    }

    public Object getFoodByCategory(String category) {
        try {
            Food.FoodCategory foodCategory = Food.FoodCategory.valueOf(category.toUpperCase());
            return foodRepository.findByCategoryAndAvailable(foodCategory, true);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid food category. Use: BREAKFAST, LUNCH, DINNER, SNACKS, BEVERAGES");
        }
    }

    public Object orderFood(Map<String, Object> orderRequest) {
        // Extract order details
        Long foodId = Long.parseLong(orderRequest.get("foodId").toString());
        Integer quantity = Integer.parseInt(orderRequest.get("quantity").toString());
        Long userId = Long.parseLong(orderRequest.get("userId").toString());
        
        // Validate food item
        Food food = foodRepository.findById(foodId)
                .orElseThrow(() -> new RuntimeException("Food item not found"));
        
        // Check availability
        if (!food.getAvailable()) {
            throw new RuntimeException("Food item is not available");
        }
        
        // Calculate total price
        Double totalPrice = food.getPrice() * quantity;
        
        // Create order response (in a real app, you'd save this to an order table)
        Map<String, Object> order = new HashMap<>();
        order.put("orderId", System.currentTimeMillis()); // Simple ID generation
        order.put("foodId", foodId);
        order.put("foodName", food.getName());
        order.put("quantity", quantity);
        order.put("unitPrice", food.getPrice());
        order.put("totalPrice", totalPrice);
        order.put("userId", userId);
        order.put("status", "ORDERED");
        order.put("message", "Food order placed successfully");
        
        return order;
    }

    public Object getFoodOrderHistory(Long userId) {
        // In a real application, you would have a separate order table
        // For now, we'll return a placeholder response
        Map<String, Object> history = new HashMap<>();
        history.put("userId", userId);
        history.put("orders", List.of());
        history.put("message", "No order history found");
        
        return history;
    }

    public String cancelFoodOrder(Long orderId) {
        // In a real application, you would update the order status in the database
        // For now, we'll return a success message
        return "Food order cancelled successfully";
    }
} 