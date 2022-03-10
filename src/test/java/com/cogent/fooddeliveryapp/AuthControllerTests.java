package com.cogent.fooddeliveryapp;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.cogent.fooddeliveryapp.controller.AuthController;
import com.cogent.fooddeliveryapp.service.CustomerService;
import com.cogent.fooddeliveryapp.service.RoleService;

/**
 * AuthControllerTests
 *
 * @author bryan
 * @date Mar 10, 2022-11:58:46 AM
 */
@ExtendWith(SpringExtension.class)
@WebMvcTest(AuthController.class)
public class AuthControllerTests {
	@Autowired
	private CustomerService customerService;
	@Autowired
	private RoleService roleService;
}
