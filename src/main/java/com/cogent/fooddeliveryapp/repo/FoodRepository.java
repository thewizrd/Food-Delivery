package com.cogent.fooddeliveryapp.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cogent.fooddeliveryapp.dto.Food;
import com.cogent.fooddeliveryapp.enums.FoodTypes;

/**
 * FoodRepository
 *
 * @author bryan
 * @date Feb 22, 2022-9:40:01 AM
 */
@Repository
public interface FoodRepository extends JpaRepository<Food, Integer> {
	List<Food> findAllByFoodType(FoodTypes foodType);
}
