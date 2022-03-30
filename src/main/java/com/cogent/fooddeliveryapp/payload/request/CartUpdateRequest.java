package com.cogent.fooddeliveryapp.payload.request;

import java.util.List;

import lombok.Data;

/**
 * CartUpdateRequest
 *
 * @author bryan
 * @date Feb 16, 2022-4:23:40 PM
 */
@Data
public class CartUpdateRequest {
	private List<Long> cart;
}