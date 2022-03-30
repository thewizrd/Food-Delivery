package com.cogent.fooddeliveryapp.dto;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * CartItem
 *
 * @author bryan
 * @date Feb 25, 2022-7:02:30 PM
 */
@Data
@EqualsAndHashCode(exclude = { "customerCart" })
@ToString(exclude = { "customerCart" })
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "cartitems")
public class CartItem {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long cartItemID;

	@ManyToOne(optional = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Food food;
	
	@OneToOne
	private CustomerCart customerCart;
	
	public CartItem(Food food, CustomerCart cart) {
		this.food = food;
		this.customerCart = cart;
	}
}
