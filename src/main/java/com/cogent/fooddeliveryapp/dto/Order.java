package com.cogent.fooddeliveryapp.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Order
 *
 * @author bryan
 * @date Apr 1, 2022-5:47:48 PM
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
public class Order {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long orderID;

	private LocalDateTime orderDate;

	@OneToOne
	private Address address;

	@ManyToOne
	private Customer customer;

	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
	private Set<OrderItem> orderItems = new HashSet<>();

	private BigDecimal orderTotal;

	public Order(Customer customer) {
		this.customer = customer;
	}
}
