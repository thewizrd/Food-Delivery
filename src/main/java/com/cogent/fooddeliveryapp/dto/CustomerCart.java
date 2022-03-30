package com.cogent.fooddeliveryapp.dto;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * CustomerCart
 *
 * @author bryan
 * @date Feb 22, 2022-12:16:48 PM
 */
@Data
@EqualsAndHashCode(exclude = { "customer" })
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = { "customer" })
@Entity
@Table
public class CustomerCart {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private boolean active;
	
	@OneToMany(mappedBy = "customerCart", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonIgnore
	private Set<CartItem> cartItems = new HashSet<>();
	
	@OneToOne(optional = false)
	private Customer customer;
	
	public CustomerCart(Customer user) {
		this.customer = user;
	}
}
