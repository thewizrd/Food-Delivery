package com.cogent.fooddeliveryapp.exceptions;

/**
 * NoRecordsFoundException
 *
 * @author bryan
 * @date Feb 21, 2022-4:27:39 PM
 */
public class NoRecordsFoundException extends Exception {
	public NoRecordsFoundException() {
		super();
	}

	public NoRecordsFoundException(String message) {
		super(message);
	}
}
