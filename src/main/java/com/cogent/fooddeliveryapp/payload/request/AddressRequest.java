package com.cogent.fooddeliveryapp.payload.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.cogent.fooddeliveryapp.dto.Address;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * AddressRequest
 *
 * @author bryan
 * @date Feb 21, 2022-10:05:07 AM
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressRequest {
	@NotNull
	private Integer houseNo;
	@NotBlank
	private String street;
	@NotBlank
	private String city;
	@NotBlank
	private String state;
	@NotBlank
	private String country;
	@NotNull
	private Integer zipCode;
	
	public AddressRequest(Address address) {
		this.houseNo = address.getHouseNo();
		this.street = address.getStreet();
		this.city = address.getCity();
		this.state = address.getState();
		this.country = address.getCountry();
		this.zipCode = address.getZipCode();
	}
}
