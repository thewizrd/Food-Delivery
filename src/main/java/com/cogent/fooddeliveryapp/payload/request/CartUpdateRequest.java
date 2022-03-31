package com.cogent.fooddeliveryapp.payload.request;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * CartUpdateRequest
 *
 * @author bryan
 * @date Feb 16, 2022-4:23:40 PM
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartUpdateRequest {
	@NotNull
	private List<Long> cart;
}