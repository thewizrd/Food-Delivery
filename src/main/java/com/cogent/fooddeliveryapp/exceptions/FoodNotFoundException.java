package com.cogent.fooddeliveryapp.exceptions;

/**
 * RoleNotFoundException
 *
 * @author bryan
 * @date Feb 21, 2022-10:23:04 AM
 */
public class FoodNotFoundException extends RuntimeException {

	public FoodNotFoundException() {
		super();
	}

	public FoodNotFoundException(String message) {
		super(message);
	}
	
	@Override
	public String toString() {
		return super.getMessage();
	}
}
