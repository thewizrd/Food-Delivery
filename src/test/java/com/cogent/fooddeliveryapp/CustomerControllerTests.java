package com.cogent.fooddeliveryapp;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import com.cogent.fooddeliveryapp.dto.Address;
import com.cogent.fooddeliveryapp.dto.Customer;
import com.cogent.fooddeliveryapp.enums.UserRoles;
import com.cogent.fooddeliveryapp.security.jwt.JwtUtils;
import com.cogent.fooddeliveryapp.service.CustomerService;
import com.cogent.fooddeliveryapp.service.RoleService;

/**
 * AuthControllerTests
 *
 * @author bryan
 * @date Mar 10, 2022-11:58:46 AM
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@ActiveProfiles("integrationtest")
public class CustomerControllerTests {
	@Autowired
	private CustomerService customerService;
	@Autowired
	private RoleService roleService;

	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private JwtUtils jwtUtils;
	
	@Autowired
	private UserDetailsService userDetailsService;

	@Test
	public void testGetAllCustomers_asAdmin() throws Exception {
		createTestCustomer("Dave");

		mockMvc.perform(
					get("/api/users")
						.contentType(MediaType.APPLICATION_JSON)
						.header(HttpHeaders.AUTHORIZATION, "Bearer " + getAuthToken("dave@email.com"))
				)
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$[0].name", is("Dave")));
	}

	private void createTestCustomer(String name) {
		Customer customer = new Customer();
		// customer.setId(1);
		customer.setName("Dave");
		customer.setEmail("dave@email.com");
		customer.setPassword(name);
		customer.setDoj(LocalDate.now());

		Address address = new Address();
		address.setCustomer(customer);
		address.setHouseNo(123);
		address.setStreet("Main Street");
		address.setCity("New York");
		address.setState("New York");
		address.setZipCode(10007);
		address.setCountry("USA");

		customer.getAddresses().add(address);

		customer.getRoles().add(roleService.getRoleByName(UserRoles.ROLE_ADMIN).get());

		customerService.addCustomer(customer);
	}
	
	private String getAuthToken(String username) {
		UserDetails userDetails = userDetailsService.loadUserByUsername(username);
		
		UsernamePasswordAuthenticationToken upAuthoken = 
				new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities()); // Credentials not needed after successful authentication
		
		return jwtUtils.generateToken(upAuthoken);
	}
}
