package com.trainmanagement.controller;

import com.trainmanagement.model.dto.ApiResponse;
import com.trainmanagement.model.dto.FoodDto;
import com.trainmanagement.service.FoodService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/food")
public class FoodController {

    @Autowired
    private FoodService foodService;

    // Get All Food Items - GET /api/food
    @GetMapping
    public ResponseEntity<ApiResponse<Object>> getAllFoodItems() {
        try {
            Object foodItems = foodService.getAllFoodItems();
            return ResponseEntity.ok(ApiResponse.success("All food items retrieved successfully", foodItems));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(ApiResponse.error("Failed to retrieve food items: " + e.getMessage(), 500, null));
        }
    }

    // Create New Food Item - POST /api/food
    @PostMapping
    public ResponseEntity<ApiResponse<Object>> createFoodItem(@Valid @RequestBody FoodDto foodDto) {
        try {
            Object foodItem = foodService.createFoodItem(foodDto);
            return ResponseEntity.status(201)
                    .body(ApiResponse.success("Food item '" + foodDto.getName() + "' created successfully", foodItem));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(ApiResponse.error("Failed to create food item: " + e.getMessage(), 500, null));
        }
    }

    // Get Food Item by ID - GET /api/food/{foodId}
    @GetMapping("/{foodId}")
    public ResponseEntity<ApiResponse<Object>> getFoodItemById(@PathVariable Long foodId) {
        try {
            Object foodItem = foodService.getFoodItemById(foodId);
            return ResponseEntity.ok(ApiResponse.success("Food item with ID " + foodId + " retrieved successfully", foodItem));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(ApiResponse.error("Failed to retrieve food item: " + e.getMessage(), 500, null));
        }
    }

    // Update Food Item - PUT /api/food/{foodId}
    @PutMapping("/{foodId}")
    public ResponseEntity<ApiResponse<Object>> updateFoodItem(
            @PathVariable Long foodId,
            @Valid @RequestBody FoodDto foodDto) {
        try {
            Object foodItem = foodService.updateFoodItem(foodId, foodDto);
            return ResponseEntity.ok(ApiResponse.success("Food item with ID " + foodId + " updated successfully", foodItem));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(ApiResponse.error("Failed to update food item: " + e.getMessage(), 500, null));
        }
    }

    // Delete Food Item - DELETE /api/food/{foodId}
    @DeleteMapping("/{foodId}")
    public ResponseEntity<ApiResponse<String>> deleteFoodItem(@PathVariable Long foodId) {
        try {
            String result = foodService.deleteFoodItem(foodId);
            return ResponseEntity.ok(ApiResponse.success("Food item with ID " + foodId + " deleted successfully", result));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(ApiResponse.error("Failed to delete food item: " + e.getMessage(), 500, null));
        }
    }

    // Get Food Items by Category - GET /api/food/category/{category}
    @GetMapping("/category/{category}")
    public ResponseEntity<ApiResponse<Object>> getFoodItemsByCategory(@PathVariable String category) {
        try {
            Object foodItems = foodService.getFoodItemsByCategory(category);
            return ResponseEntity.ok(ApiResponse.success("Food items in category '" + category + "' retrieved successfully", foodItems));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(ApiResponse.error("Failed to retrieve food items: " + e.getMessage(), 500, null));
        }
    }

    // Get Available Food Items - GET /api/food/available
    @GetMapping("/available")
    public ResponseEntity<ApiResponse<Object>> getAvailableFoodItems() {
        try {
            Object foodItems = foodService.getAvailableFoodItems();
            return ResponseEntity.ok(ApiResponse.success("All available food items retrieved successfully", foodItems));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(ApiResponse.error("Failed to retrieve available food items: " + e.getMessage(), 500, null));
        }
    }
} 