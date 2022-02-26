package com.cogent.fooddeliveryapp.payload.response;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

import com.cogent.fooddeliveryapp.dto.CustomerCart;

import lombok.Data;

/**
 * CartStatusResponse
 *
 * @author bryan
 * @date Feb 16, 2022-4:19:28 PM
 */
@Data
public class CartStatusResponse {
	private Set<FoodResponse> cart;
	private String status;
	
	public CartStatusResponse(CustomerCart customerCart) {
		if (customerCart.getCartItems() != null) {
			this.cart = customerCart.getCartItems().stream().map(cartItem -> {
				return new FoodResponse(cartItem.getFood());
			}).collect(Collectors.toSet());
		} else {
			this.cart = Collections.emptySet();
		}
		this.status = customerCart.isActive() ? "active" : "not active";
	}
}

