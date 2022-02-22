package com.cogent.fooddeliveryapp.dto;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;

/**
 * CustomerCart
 *
 * @author bryan
 * @date Feb 22, 2022-12:16:48 PM
 */
//@Data
//@AllArgsConstructor
//@NoArgsConstructor
//@Entity
//@Table
public class CustomerCart {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private boolean status;
	
	//@ManyToMany(cascade = CascadeType.ALL, mappedBy = "")
	private Set<Food> cartItems;
}
