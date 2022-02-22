package com.cogent.fooddeliveryapp.exceptions;

/**
 * RoleNotFoundException
 *
 * @author bryan
 * @date Feb 21, 2022-10:23:04 AM
 */
public class RoleNotFoundException extends RuntimeException {

	public RoleNotFoundException() {
		super();
	}

	public RoleNotFoundException(String message) {
		super(message);
	}
	
	@Override
	public String toString() {
		return super.getMessage();
	}
}
