package com.cogent.fooddeliveryapp.dto;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

import com.cogent.fooddeliveryapp.enums.FoodTypes;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Food
 *
 * @author bryan
 * @date Feb 22, 2022-9:24:42 AM
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table
public class Food {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	@NotBlank
	private String name;
	@PositiveOrZero
	@NotNull
	private BigDecimal foodCost;
	@NotNull
	@Enumerated(EnumType.STRING)
	private FoodTypes foodType;
	@NotBlank
	private String description;
	@NotBlank
	private String foodPicURL;
}
