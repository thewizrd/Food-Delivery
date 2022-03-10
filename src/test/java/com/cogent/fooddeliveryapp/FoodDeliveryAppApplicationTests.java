package com.cogent.fooddeliveryapp;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.cogent.fooddeliveryapp.controller.AuthController;
import com.cogent.fooddeliveryapp.controller.CustomerController;
import com.cogent.fooddeliveryapp.controller.FoodController;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class FoodDeliveryAppApplicationTests {
	
	@Autowired
	private AuthController authController;
	@Autowired
	private CustomerController customerController;
	@Autowired
	private FoodController foodController;

	@Test
	void contextLoads() {
		Assertions.assertThat(authController).isNot(null);
		Assertions.assertThat(customerController).isNot(null);
		Assertions.assertThat(foodController).isNot(null);
	}

}
