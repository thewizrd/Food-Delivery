package com.cogent.fooddeliveryapp.service;

import java.util.List;
import java.util.Optional;

import com.cogent.fooddeliveryapp.dto.Food;
import com.cogent.fooddeliveryapp.enums.FoodTypes;
import com.cogent.fooddeliveryapp.exceptions.NoRecordsFoundException;

/**
 * FoodService
 *
 * @author bryan
 * @date Feb 22, 2022-9:41:47 AM
 */
public interface FoodService {
	Food addFood(Food food);
	Optional<Food> getFoodByID(int id);
	List<Food> getAllFoods();
	List<Food> getAllFoodsByFoodType(FoodTypes foodType);
	List<Food> getAllFoodsAscByID();
	List<Food> getAllFoodsDescByID();
	boolean deleteFoodByID(int id);
	Food updateFood(Food food) throws NoRecordsFoundException;
	boolean existsByID(int id);
}
