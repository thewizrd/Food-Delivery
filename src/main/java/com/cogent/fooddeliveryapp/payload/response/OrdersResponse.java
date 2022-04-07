package com.cogent.fooddeliveryapp.payload.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.cogent.fooddeliveryapp.dto.Order;
import com.cogent.fooddeliveryapp.payload.request.AddressRequest;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

import lombok.Data;

/**
 * OrdersResponse
 *
 * @author bryan
 * @date Apr 1, 2022-6:15:45 PM
 */
@Data
public class OrdersResponse {
	private long orderID;
	@JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime orderDate;
	private AddressRequest address;
	private List<FoodResponse> orderItems;
	private BigDecimal orderTotal;
	
	public OrdersResponse(Order order) {
		this.orderID = order.getOrderID();
		this.orderDate = order.getOrderDate();
		this.address = new AddressRequest(order.getAddress());
		this.orderItems = order.getOrderItems().stream().map((oi) -> {
			return new FoodResponse(oi.getFood());
		}).collect(Collectors.toList());
		this.orderTotal = order.getOrderTotal();
	}
}
