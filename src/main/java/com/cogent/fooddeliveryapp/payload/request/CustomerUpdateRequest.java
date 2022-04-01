package com.cogent.fooddeliveryapp.payload.request;

import java.util.Set;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

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
public class CustomerUpdateRequest {
	@NotBlank
	private String name;
	@NotNull
	private Set<AddressRequest> address;
}
