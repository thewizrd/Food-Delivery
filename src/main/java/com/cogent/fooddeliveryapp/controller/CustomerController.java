package com.cogent.fooddeliveryapp.controller;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cogent.fooddeliveryapp.dto.Address;
import com.cogent.fooddeliveryapp.dto.Customer;
import com.cogent.fooddeliveryapp.dto.Role;
import com.cogent.fooddeliveryapp.enums.UserRoles;
import com.cogent.fooddeliveryapp.exceptions.CustomerNotFoundException;
import com.cogent.fooddeliveryapp.exceptions.InvalidRequestException;
import com.cogent.fooddeliveryapp.exceptions.NoRecordsFoundException;
import com.cogent.fooddeliveryapp.exceptions.RoleNotFoundException;
import com.cogent.fooddeliveryapp.payload.request.AddressRequest;
import com.cogent.fooddeliveryapp.payload.request.CustomerRegistrationRequest;
import com.cogent.fooddeliveryapp.payload.request.LoginRequest;
import com.cogent.fooddeliveryapp.payload.response.ApiMessage;
import com.cogent.fooddeliveryapp.payload.response.CustomerResponse;
import com.cogent.fooddeliveryapp.service.CustomerService;
import com.cogent.fooddeliveryapp.service.RoleService;

/**
 * CustomerController
 *
 * @author bryan
 * @date Feb 18, 2022-4:46:51 PM
 */
@RestController
@RequestMapping("/api/users")
public class CustomerController {
	@Autowired
	private CustomerService customerService;
	@Autowired
	private RoleService roleService;
	@Autowired
	private Function<AddressRequest, Address> addressMapper;
	
	@PostMapping("/register")
	public ResponseEntity<?> createUser(@Valid @RequestBody CustomerRegistrationRequest request) throws InvalidRequestException {
		if (customerService.existsByEmail(request.getEmail())) {
			Customer customer = new Customer();
			customer.setEmail(request.getEmail());
			customer.setPassword(request.getPassword());
			customer.setName(request.getName());
			customer.setDoj(request.getDoj());		
			customer.setAddresses(request.getAddress().parallelStream().map(addressRequest -> {
				Address address = addressMapper.apply(addressRequest);
				address.setCustomer(customer);
				return address;
			}).collect(Collectors.toSet()));
			customer.setRoles(request.getRoles().parallelStream().map(roleName -> {
				Role role = null;
				
				switch (roleName) {
					case "admin":
						role = roleService.getRoleByName(UserRoles.ROLE_ADMIN).orElseThrow(() -> {
							throw new RoleNotFoundException("Role name: " + roleName + " not found");
						});
						break;
					case "user":
					default:
						role = roleService.getRoleByName(UserRoles.ROLE_USER).orElseThrow(() -> {
							throw new RoleNotFoundException("Role name: " + roleName + " not found");
						});
						break;
				}
				
				return role;
			}).collect(Collectors.toSet()));
			
			Customer created = customerService.addCustomer(customer);
			
			return ResponseEntity.status(HttpStatus.CREATED).body(created);
		} else {
			throw new InvalidRequestException("User with email address already exists");
		}
	}
	
	@GetMapping
	public ResponseEntity<?> getUsers() throws NoRecordsFoundException {
		List<Customer> customers = customerService.getAllCustomers();
		
		if (customers != null && !customers.isEmpty()) {
			return ResponseEntity.ok(customers.parallelStream().map(user -> {
				return new CustomerResponse(user);
			}).sorted((o1, o2) -> {
				return Integer.compare(o1.getId(), o2.getId());
			}).collect(Collectors.toList()));
		} else {
			throw new NoRecordsFoundException("No Customer records found");
		}
	}
	
	@GetMapping("/{userID}")
	public ResponseEntity<?> getUserByID(@PathVariable int userID) throws NoRecordsFoundException {
		Optional<Customer> customerOpt = customerService.getCustomerByID(userID);
		if (customerOpt.isPresent()) {
			return ResponseEntity.ok(new CustomerResponse(customerOpt.get()));
		} else {
			throw new NoRecordsFoundException("Customer with ID: " + userID + " not found");
		}
	}
	
	@PutMapping("/{userID}")
	public ResponseEntity<?> updateUserByID(@PathVariable int userID, @RequestBody CustomerRegistrationRequest request) throws NoRecordsFoundException, CustomerNotFoundException, InvalidRequestException {
		Optional<Customer> customerOpt = customerService.getCustomerByID(userID);
		if (customerOpt.isPresent()) {
			final Customer customer = customerOpt.get();
			
			if (!Objects.equals(customer.getEmail(), request.getEmail())) {
				throw new InvalidRequestException("Email address does not match. Email address must remain the same");
			}
			
			// Update user details
			customer.setEmail(request.getEmail());
			customer.setPassword(request.getPassword());
			customer.setName(request.getName());
			customer.setDoj(request.getDoj());		
			customer.setAddresses(request.getAddress().parallelStream().map(addressRequest -> {
				Address address = addressMapper.apply(addressRequest);
				address.setCustomer(customer);
				return address;
			}).collect(Collectors.toSet()));
			customer.setRoles(request.getRoles().parallelStream().map(roleName -> {
				Role role = null;
				
				switch (roleName) {
					case "admin":
						role = roleService.getRoleByName(UserRoles.ROLE_ADMIN).orElseThrow(() -> {
							throw new RoleNotFoundException("Role name: " + roleName + " not found");
						});
						break;
					case "user":
					default:
						role = roleService.getRoleByName(UserRoles.ROLE_USER).orElseThrow(() -> {
							throw new RoleNotFoundException("Role name: " + roleName + " not found");
						});
						break;
				}
				
				return role;
			}).collect(Collectors.toSet()));
			
			Customer updated = customerService.updateCustomer(customer);
			
			return ResponseEntity.ok(new CustomerResponse(updated));
		} else {
			throw new NoRecordsFoundException("Customer with ID: " + userID + " not found");
		}
	}
	
	@DeleteMapping("/{userID}")
	public ResponseEntity<?> deleteUserByID(@PathVariable int userID) throws NoRecordsFoundException {
		boolean exists = customerService.existsByID(userID);
		if (exists) {
			customerService.deleteCustomerByID(userID);
			// return ResponseEntity.ok(new ApiMessage("User deleted successfully"));
			return ResponseEntity.noContent().build();
		} else {
			throw new NoRecordsFoundException("Customer with ID: " + userID + " not found");
		}
	}
	
	// TODO: Authentication (Spring Security)
	@PostMapping("/authenticate")
	public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest request) {
		Optional<Customer> customerOpt = customerService.getCustomerByEmail(request.getEmail());

		if (customerOpt.isPresent()) {
			if (Objects.equals(request.getPassword(), customerOpt.get().getPassword())) {
				return ResponseEntity.ok("JWT Token");
			}
		}
		
		return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ApiMessage("Access denied"));
	}
}
