package com.cogent.fooddeliveryapp.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.cogent.fooddeliveryapp.dto.Food;
import com.cogent.fooddeliveryapp.enums.FoodTypes;
import com.cogent.fooddeliveryapp.exceptions.NoRecordsFoundException;
import com.cogent.fooddeliveryapp.repo.FoodRepository;
import com.cogent.fooddeliveryapp.service.FoodService;

/**
 * FoodServiceImpl
 *
 * @author bryan
 * @date Feb 22, 2022-9:42:39 AM
 */
@Service
public class FoodServiceImpl implements FoodService {
	@Autowired
	private FoodRepository repo;
	

	@Override
	public Food addFood(Food food) {
		return repo.save(food);
	}

	@Override
	public Optional<Food> getFoodByID(int id) {
		return repo.findById(id);
	}

	@Override
	public List<Food> getAllFoods() {
		return repo.findAll();
	}
	
	@Override
	public List<Food> getAllFoodsByFoodType(FoodTypes foodType) {
		return repo.findAllByFoodType(foodType);
	}
	
	@Override
	public List<Food> getAllFoodsAscByID() {
		return repo.findAll(Sort.by(Sort.Order.asc("id")));
	}
	
	@Override
	public List<Food> getAllFoodsDescByID() {
		return repo.findAll(Sort.by(Sort.Order.desc("id")));
	}

	@Override
	public boolean deleteFoodByID(int id) {
		Food foodRef = repo.getById(id);
		repo.delete(foodRef);
		return true;
	}

	@Override
	public Food updateFood(Food food) throws NoRecordsFoundException {
		if (repo.existsById(food.getId())) {
			return repo.save(food);
		} else {
			throw new NoRecordsFoundException("Food with id: " + food.getId() + " not found");
		}
	}

	@Override
	public boolean existsByID(int id) {
		return repo.existsById(id);
	}

}
