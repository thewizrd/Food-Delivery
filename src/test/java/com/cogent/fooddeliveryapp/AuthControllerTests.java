package com.cogent.fooddeliveryapp;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.cogent.fooddeliveryapp.controller.AuthController;

/**
 * AuthControllerTests
 *
 * @author bryan
 * @date Mar 10, 2022-11:58:46 AM
 */
@ExtendWith(SpringExtension.class)
@WebMvcTest(AuthController.class)
public class AuthControllerTests {
}
