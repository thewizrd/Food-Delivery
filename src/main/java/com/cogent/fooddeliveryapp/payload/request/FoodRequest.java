package com.cogent.fooddeliveryapp.payload.request;

import java.math.BigDecimal;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

import com.cogent.fooddeliveryapp.enums.FoodTypes;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Feature;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * FoodRequest
 *
 * @author bryan
 * @date Feb 22, 2022-9:43:28 AM
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FoodRequest {
	@NotBlank
	private String foodName;
	@PositiveOrZero
	@NotNull
	private BigDecimal foodCost;
	@NotNull
	@Enumerated(EnumType.STRING)
	@JsonFormat(with = { Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, Feature.ACCEPT_CASE_INSENSITIVE_VALUES }) 
	private FoodTypes foodType;
	@NotBlank
	private String description;
	@NotBlank
	private String foodPic;
}
