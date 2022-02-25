package com.cogent.fooddeliveryapp.advice;

import javax.validation.ConstraintViolationException;

import org.springframework.beans.TypeMismatchException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.cogent.fooddeliveryapp.exceptions.InvalidRequestException;
import com.cogent.fooddeliveryapp.exceptions.NoRecordsFoundException;
import com.cogent.fooddeliveryapp.exceptions.RoleNotFoundException;
import com.cogent.fooddeliveryapp.payload.response.ApiError;

/**
 * ControllerAdvices
 *
 * @author bryan
 * @date Feb 21, 2022-10:56:58 AM
 */
@ControllerAdvice
public class ControllerAdvices extends ResponseEntityExceptionHandler {
	@ExceptionHandler(RoleNotFoundException.class)
	public ResponseEntity<?> handleRoleNotFoundException(RoleNotFoundException e) {
		ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST);
		apiError.setMessage(e.getMessage());
		return buildResponseEntity(apiError);
	}
	
	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<?> handleDataIntegrityViolationException(DataIntegrityViolationException e) {
		ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST);
		apiError.setMessage(e.getMessage());
		return buildResponseEntity(apiError);
	}
	
	@ExceptionHandler(NoRecordsFoundException.class)
	public ResponseEntity<?> handleNoRecordsFoundException(NoRecordsFoundException e) {
		ApiError apiError = new ApiError(HttpStatus.NOT_FOUND);
		apiError.setMessage(e.getMessage());
		return buildResponseEntity(apiError);
	}
	
	@ExceptionHandler(InvalidRequestException.class)
	public ResponseEntity<?> handleInvalidRequestException(InvalidRequestException e) {
		ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST);
		apiError.setMessage(e.getMessage());
		return buildResponseEntity(apiError);
	}
	
	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<?> handleConstraintViolationException(ConstraintViolationException e) {
		ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST);
		apiError.setMessage("Validation Error");
		apiError.addValidationErrors(e.getConstraintViolations());

		return buildResponseEntity(apiError);
	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		// @Valid - validation failed
		ApiError apiError = new ApiError(status);
		apiError.setMessage("Validation Error");
		apiError.addValidationErrors(ex.getFieldErrors());
		apiError.addValidationObjectErrors(ex.getBindingResult().getGlobalErrors());
		
		return buildResponseEntity(apiError);
	}
	
	@Override
	protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers,
			HttpStatus status, WebRequest request) {
		ApiError apiError = new ApiError(status);
		apiError.setMessage(ex.getMessage());
		apiError.setDebugMessage(ex.getRequiredType().getName());
		
		return buildResponseEntity(apiError);
	}
	
	/*
	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<?> handleAccessDeniedException(AccessDeniedException e) {
		ApiError apiError = new ApiError(HttpStatus.FORBIDDEN);
		apiError.setMessage(e.getMessage());
		return buildResponseEntity(apiError);
	}
	*/
	
	/*
	@ExceptionHandler(Exception.class)
	public ResponseEntity<?> handleGenericException(Exception e) {
		ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR);
		apiError.setMessage(e.getMessage());
		return buildResponseEntity(apiError);
	}
	*/
	
	private ResponseEntity<Object> buildResponseEntity(ApiError error) {
		return ResponseEntity.status(error.getStatus()).body(error);
	}
}
