package com.cogent.fooddeliveryapp.dto;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Customer
 *
 * @author bryan
 * @date Feb 18, 2022-2:49:13 PM
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = { "addresses", "orders" })
@EqualsAndHashCode(callSuper = true)
@Entity
@Table
public class Customer extends User {
	@NotNull
	@OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
	private Set<Address> addresses = new HashSet<>();
	
	@OneToOne(mappedBy = "customer", cascade = CascadeType.ALL)
	@JsonIgnore
	private CustomerCart customerCart = new CustomerCart(this);
	
	@OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
	private List<Order> orders = new ArrayList<>();
}
