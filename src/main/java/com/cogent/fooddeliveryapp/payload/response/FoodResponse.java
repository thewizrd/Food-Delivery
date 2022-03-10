package com.cogent.fooddeliveryapp.payload.response;

import java.math.BigDecimal;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

import com.cogent.fooddeliveryapp.dto.Food;
import com.cogent.fooddeliveryapp.enums.FoodTypes;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * FoodResponse
 *
 * @author bryan
 * @date Feb 22, 2022-10:10:36 AM
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FoodResponse {
	private long foodID;
	@NotBlank
	private String foodName;
	@PositiveOrZero
	@NotNull
	private BigDecimal foodCost;
	@NotNull
	@Enumerated(EnumType.STRING)
	private FoodTypes foodType;
	@NotBlank
	private String description;
	@NotBlank
	private String foodPic;
	
	public FoodResponse(Food food) {
		this.foodID = food.getId();
		this.foodName = food.getName();
		this.foodCost = food.getFoodCost();
		this.foodType = food.getFoodType();
		this.description = food.getDescription();
		this.foodPic = food.getFoodPicURL();
	}
}
