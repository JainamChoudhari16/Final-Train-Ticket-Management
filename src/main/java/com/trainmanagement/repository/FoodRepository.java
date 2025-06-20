package com.trainmanagement.repository;

import com.trainmanagement.model.Food;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface FoodRepository extends JpaRepository<Food, Long> {
    
    List<Food> findByAvailable(Boolean available);
    
    List<Food> findByCategory(Food.FoodCategory category);
    
    List<Food> findByCategoryAndAvailable(Food.FoodCategory category, Boolean available);
    
    List<Food> findByPriceBetween(Double minPrice, Double maxPrice);
} 