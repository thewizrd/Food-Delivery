package com.cogent.fooddeliveryapp.exceptions;

/**
 * InvalidRequestException
 *
 * @author bryan
 * @date Feb 21, 2022-5:44:22 PM
 */
public class InvalidRequestException extends Exception {
	public InvalidRequestException() {
		super();
	}

	public InvalidRequestException(String message) {
		super(message);
	}
}
