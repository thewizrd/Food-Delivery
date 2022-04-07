package com.cogent.fooddeliveryapp.dto;

import java.math.BigDecimal;

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
 * OrderItem
 *
 * @author bryan
 * @date Apr 7, 2022-5:02:45 PM
 */
@Data
@EqualsAndHashCode(exclude = { "order" })
@ToString(exclude = { "order" })
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "orderitems")
public class OrderItem {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long orderItemID;

	@ManyToOne(optional = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Food food;

	private BigDecimal itemCost;
	
	@OneToOne
	private Order order;
	
	public OrderItem(Food food, Order order) {
		this.food = food;
		this.itemCost = food.getFoodCost();

		this.order = order;
	}
}
