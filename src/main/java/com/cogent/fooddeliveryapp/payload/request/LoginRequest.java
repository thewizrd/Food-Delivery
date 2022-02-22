package com.cogent.fooddeliveryapp.payload.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * LoginRequest
 *
 * @author bryan
 * @date Feb 21, 2022-9:00:45 PM
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {
	@Email
	@NotBlank
	private String email;
	@NotBlank
	private String password;
}
