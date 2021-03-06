package com.cogent.fooddeliveryapp.controller;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;
import javax.validation.constraints.Min;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cogent.fooddeliveryapp.dto.Food;
import com.cogent.fooddeliveryapp.enums.FoodTypes;
import com.cogent.fooddeliveryapp.exceptions.NoRecordsFoundException;
import com.cogent.fooddeliveryapp.payload.request.CartUpdateRequest;
import com.cogent.fooddeliveryapp.payload.request.FoodRequest;
import com.cogent.fooddeliveryapp.payload.response.FoodResponse;
import com.cogent.fooddeliveryapp.service.FoodService;

/**
 * FoodController
 *
 * @author bryan
 * @date Feb 22, 2022-9:23:14 AM
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/food")
// @RestController == @Controller + @ResponseBody
@Validated // Needed to validate @RequestParam and @PathVariable
public class FoodController {
	@Autowired
	private FoodService foodService;

	@PostMapping
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<?> addFoodItem(@Valid @RequestBody FoodRequest request) {
		Food food = new Food();
		food.setName(request.getFoodName());
		food.setFoodCost(request.getFoodCost());
		food.setFoodType(request.getFoodType());
		food.setDescription(request.getDescription());
		food.setFoodPicURL(request.getFoodPic());

		Food created = foodService.addFood(food);

		// HTTP 201: Create for new entities
		return ResponseEntity.status(HttpStatus.CREATED).body(new FoodResponse(created));
	}

	@GetMapping("/get/id/{foodID}")
	public ResponseEntity<?> getFoodItemByID(@PathVariable @Min(1) Long foodID) throws NoRecordsFoundException {
		Food item = foodService.getFoodByID(foodID).orElseThrow(() -> {
			return new NoRecordsFoundException("Food item with ID: " + foodID + " not found");
		});

		return ResponseEntity.ok(new FoodResponse(item));
	}

	@PutMapping("/id/{foodID}")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<?> updateFoodItemByID(@PathVariable @Min(1) Long foodID,
			@Valid @RequestBody FoodRequest request) throws NoRecordsFoundException {
		Food item = foodService.getFoodByID(foodID).orElseThrow(() -> {
			return new NoRecordsFoundException("Food item with ID: " + foodID + " not found");
		});

		item.setName(request.getFoodName());
		item.setFoodCost(request.getFoodCost());
		item.setFoodType(request.getFoodType());
		item.setDescription(request.getDescription());
		item.setFoodPicURL(request.getFoodPic());

		Food updated = foodService.updateFood(item);

		// Either 200: OK or 201: No Content
		return ResponseEntity.ok(new FoodResponse(updated));
	}

	@GetMapping("/get")
	public ResponseEntity<?> getAllFoodItems() throws NoRecordsFoundException {
		List<Food> foods = foodService.getAllFoods();

		if (foods != null) {
			return ResponseEntity.ok(foods.stream().map(f -> {
				return new FoodResponse(f);
			}).collect(Collectors.toList()));
		} else {
			return ResponseEntity.ok(Collections.emptyList());
		}
	}

	@GetMapping("/get/asc")
	public ResponseEntity<?> getAllFoodItemsAsc() throws NoRecordsFoundException {
		List<Food> foods = foodService.getAllFoodsAscByID();

		if (foods != null) {
			return ResponseEntity.ok(foods.stream().map(f -> {
				return new FoodResponse(f);
			}).collect(Collectors.toList()));
		} else {
			return ResponseEntity.ok(Collections.emptyList());
		}
	}

	@GetMapping("/get/desc")
	public ResponseEntity<?> getAllFoodItemsDesc() throws NoRecordsFoundException {
		List<Food> foods = foodService.getAllFoodsDescByID();

		if (foods != null) {
			return ResponseEntity.ok(foods.stream().map(f -> {
				return new FoodResponse(f);
			}).collect(Collectors.toList()));
		} else {
			return ResponseEntity.ok(Collections.emptyList());
		}
	}

	@PostMapping("/get/cart")
	public ResponseEntity<?> getCartItemsDetails(@Valid @RequestBody CartUpdateRequest request)
			throws NoRecordsFoundException {
		if (request.getCart() != null) {
			return ResponseEntity.ok(request.getCart().stream().map(f -> {
				try {
					Food foodItem = foodService.getFoodByID(f).orElseThrow(() -> {
						return new NoRecordsFoundException("Food item with ID: " + f + " not found");
					});
					return new FoodResponse(foodItem);
				} catch (NoRecordsFoundException e) {
					throw new RuntimeException(e);
				}
			}).collect(Collectors.toList()));
		} else {
			return ResponseEntity.ok(Collections.emptyList());
		}
	}

	@GetMapping("/get/foodType")
	public ResponseEntity<?> getFoodTypes() throws NoRecordsFoundException {
		return ResponseEntity.ok(FoodTypes.values());
	}

	@GetMapping("/get/foodType/{foodType}")
	public ResponseEntity<?> getAllFoodItemsByType(@PathVariable FoodTypes foodType) throws NoRecordsFoundException {
		List<Food> foods = foodService.getAllFoodsByFoodType(foodType);

		if (foods != null) {
			return ResponseEntity.ok(foods.stream().map(f -> {
				return new FoodResponse(f);
			}).collect(Collectors.toList()));
		} else {
			return ResponseEntity.ok(Collections.emptyList());
		}
	}

	@DeleteMapping("/id/{foodID}")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<?> deleteFoodItemByID(@PathVariable @Min(1) Long foodID) throws NoRecordsFoundException {
		if (foodService.existsByID(foodID)) {
			foodService.deleteFoodByID(foodID);
			// Use 204: No content for deletion
			return ResponseEntity.noContent().build();
		} else {
			throw new NoRecordsFoundException("Food item with ID: " + foodID + " not found");
		}
	}
}
