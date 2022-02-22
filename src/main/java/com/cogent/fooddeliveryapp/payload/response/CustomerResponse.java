package com.cogent.fooddeliveryapp.payload.response;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import com.cogent.fooddeliveryapp.dto.Customer;
import com.cogent.fooddeliveryapp.enums.UserRoles;
import com.cogent.fooddeliveryapp.payload.request.AddressRequest;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

import lombok.Data;

/**
 * CustomerResponse
 *
 * @author bryan
 * @date Feb 21, 2022-3:27:10 PM
 */
@Data
public class CustomerResponse {
	private int id;	
	private String email;
	private String name;
	@JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd")
	private LocalDate doj;
	private Set<AddressRequest> address;
	@Enumerated(EnumType.STRING)
	private Set<UserRoles> roles;
	
	public CustomerResponse(Customer user) {
		this.id = user.getId();
		this.email = user.getEmail();
		this.name = user.getName();
		this.doj = user.getDoj();
		
		if (user.getAddresses() != null) {
			address = new HashSet<>();
			user.getAddresses().forEach(address -> {
				this.address.add(new AddressRequest(address));
			});
		}
		
		if (user.getRoles() != null) {
			roles = new HashSet<>();
			user.getRoles().forEach(role -> {
				if (role != null) {
					this.roles.add(role.getRoleName());
				}
			});
		}
	}
}
