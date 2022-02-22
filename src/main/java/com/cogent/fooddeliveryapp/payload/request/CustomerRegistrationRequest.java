package com.cogent.fooddeliveryapp.payload.request;

import java.time.LocalDate;
import java.util.Set;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * CustomerRegistrationRequest
 *
 * @author bryan
 * @date Feb 18, 2022-2:39:12 PM
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerRegistrationRequest {
	@NotBlank
	@Email
	private String email;
	@NotBlank
	private String password;
	@NotBlank
	private String name;
	@NotNull
	private Set<AddressRequest> address;
	@NotNull
	@JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd")
	private LocalDate doj;
	@NotEmpty
	//@Pattern(regexp = "user|admin", flags = Pattern.Flag.CASE_INSENSITIVE)
	private Set<String> roles;
}
