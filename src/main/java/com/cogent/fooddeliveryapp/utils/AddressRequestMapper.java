package com.cogent.fooddeliveryapp.utils;

import java.util.function.Function;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.cogent.fooddeliveryapp.dto.Address;
import com.cogent.fooddeliveryapp.payload.request.AddressRequest;

/**
 * AddressRequestMapper
 *
 * @author bryan
 * @date Feb 21, 2022-10:06:19 AM
 */
@Component
public class AddressRequestMapper {
	@Bean
	public Function<AddressRequest, Address> addressMapper() {
		return (addrRequest) -> {
			Address address = new Address();
			address.setHouseNo(addrRequest.getHouseNo());
			address.setStreet(addrRequest.getStreet());
			address.setCity(addrRequest.getCity());
			address.setState(addrRequest.getState());
			address.setCountry(addrRequest.getCountry());
			address.setZipCode(addrRequest.getZipCode());
			
			return address;
		};
	}
}
