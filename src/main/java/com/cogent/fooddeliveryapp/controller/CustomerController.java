package com.cogent.fooddeliveryapp.controller;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cogent.fooddeliveryapp.dto.Address;
import com.cogent.fooddeliveryapp.dto.CartItem;
import com.cogent.fooddeliveryapp.dto.Customer;
import com.cogent.fooddeliveryapp.dto.CustomerCart;
import com.cogent.fooddeliveryapp.dto.Food;
import com.cogent.fooddeliveryapp.dto.Role;
import com.cogent.fooddeliveryapp.enums.UserRoles;
import com.cogent.fooddeliveryapp.exceptions.CustomerNotFoundException;
import com.cogent.fooddeliveryapp.exceptions.FoodNotFoundException;
import com.cogent.fooddeliveryapp.exceptions.InvalidRequestException;
import com.cogent.fooddeliveryapp.exceptions.NoRecordsFoundException;
import com.cogent.fooddeliveryapp.exceptions.RoleNotFoundException;
import com.cogent.fooddeliveryapp.payload.request.AddressRequest;
import com.cogent.fooddeliveryapp.payload.request.CartUpdateRequest;
import com.cogent.fooddeliveryapp.payload.request.CustomerRegistrationRequest;
import com.cogent.fooddeliveryapp.payload.response.CartStatusResponse;
import com.cogent.fooddeliveryapp.payload.response.CustomerResponse;
import com.cogent.fooddeliveryapp.security.services.UserDetailsImpl;
import com.cogent.fooddeliveryapp.service.CustomerService;
import com.cogent.fooddeliveryapp.service.FoodService;
import com.cogent.fooddeliveryapp.service.RoleService;

/**
 * CustomerController
 *
 * @author bryan
 * @date Feb 18, 2022-4:46:51 PM
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/users")
public class CustomerController {
	@Autowired
	private CustomerService customerService;
	@Autowired
	private RoleService roleService;
	@Autowired
	private Function<AddressRequest, Address> addressMapper;

	@Autowired
	private FoodService foodService;
	
	private Authentication getAuthentication() {
		return SecurityContextHolder.getContext().getAuthentication();
	}
	
	// Checks if user has access to this item
	// Only ADMINs or the specified user can access
	private void checkUserAccess(Long userID) {
		Authentication auth = getAuthentication();
		UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();
		
		if (!userDetails.isAdmin() && userDetails.getId() != userID) {
			throw new AccessDeniedException("User unable to access this resource");
		}
	}

	@GetMapping
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<?> getUsers() throws NoRecordsFoundException {
		List<Customer> customers = customerService.getAllCustomers();
		
		if (customers != null && !customers.isEmpty()) {
			return ResponseEntity.ok(customers.stream().map(user -> {
				return new CustomerResponse(user);
			}).sorted((o1, o2) -> {
				return Long.compare(o1.getId(), o2.getId());
			}).collect(Collectors.toList()));
		} else {
			throw new NoRecordsFoundException("No Customer records found");
		}
	}
	
	@GetMapping("/{userID}")
	@PreAuthorize("hasRole('ROLE_ADMIN') || hasRole('ROLE_USER')")
	public ResponseEntity<?> getUserByID(@PathVariable Long userID) throws NoRecordsFoundException {
		checkUserAccess(userID);
		
		Optional<Customer> customerOpt = customerService.getCustomerByID(userID);
		if (customerOpt.isPresent()) {
			return ResponseEntity.ok(new CustomerResponse(customerOpt.get()));
		} else {
			throw new NoRecordsFoundException("Customer with ID: " + userID + " not found");
		}
	}
	
	@PutMapping("/{userID}")
	@PreAuthorize("hasRole('ROLE_ADMIN') || hasRole('ROLE_USER')")
	public ResponseEntity<?> updateUserByID(@PathVariable Long userID, @Valid @RequestBody CustomerRegistrationRequest request) throws NoRecordsFoundException, CustomerNotFoundException, InvalidRequestException {
		checkUserAccess(userID);
		
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
			customer.setAddresses(request.getAddress().stream().map(addressRequest -> {
				Address address = addressMapper.apply(addressRequest);
				address.setCustomer(customer);
				return address;
			}).collect(Collectors.toSet()));
			customer.setRoles(request.getRoles().parallelStream().map(roleName -> {
				Role role = null;
				
				switch (roleName) {
					case "admin":
						role = roleService.getRoleByName(UserRoles.ROLE_ADMIN).orElseThrow(() -> {
							return new RoleNotFoundException("Role name: " + roleName + " not found");
						});
						break;
					case "user":
					default:
						role = roleService.getRoleByName(UserRoles.ROLE_USER).orElseThrow(() -> {
							return new RoleNotFoundException("Role name: " + roleName + " not found");
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
	@PreAuthorize("hasRole('ROLE_ADMIN') || hasRole('ROLE_USER')")
	public ResponseEntity<?> deleteUserByID(@PathVariable Long userID) throws NoRecordsFoundException {
		checkUserAccess(userID);

		boolean exists = customerService.existsByID(userID);
		if (exists) {
			customerService.deleteCustomerByID(userID);
			// return ResponseEntity.ok(new ApiMessage("User deleted successfully"));
			return ResponseEntity.noContent().build();
		} else {
			throw new NoRecordsFoundException("Customer with ID: " + userID + " not found");
		}
	}
	
	/* Customer Cart operations */
	@GetMapping("/{userID}/cart")
	@PreAuthorize("hasRole('ROLE_ADMIN') || hasRole('ROLE_USER')")
	public ResponseEntity<?> getUserCartByID(@PathVariable Long userID) throws NoRecordsFoundException, CustomerNotFoundException {
		checkUserAccess(userID);
		
		Customer customer = customerService.getCustomerByID(userID).orElseThrow(() -> {
			return new NoRecordsFoundException("Customer with ID: " + userID + " not found");
		});
		
		return ResponseEntity.ok(new CartStatusResponse(customer.getCustomerCart()));
	}
	
	@PutMapping("/{userID}/cart")
	@PreAuthorize("hasRole('ROLE_ADMIN') || hasRole('ROLE_USER')")
	public ResponseEntity<?> updateUserCartByID(@PathVariable Long userID, @Valid @RequestBody CartUpdateRequest request) throws NoRecordsFoundException, CustomerNotFoundException {
		checkUserAccess(userID);
		
		Customer customer = customerService.getCustomerByID(userID).orElseThrow(() -> {
			return new NoRecordsFoundException("Customer with ID: " + userID + " not found");
		});
		
		CustomerCart cart = customer.getCustomerCart();
		cart.getCartItems().clear();
		cart.getCartItems().addAll(request.getCart().stream().map(foodID -> {
			Food foodItem = foodService.getFoodByID(foodID).orElseThrow(() -> {
				return new FoodNotFoundException("Food with ID: " + foodID + " not found");
			});
			
			return new CartItem(foodItem, cart);
		}).collect(Collectors.toSet()));
		cart.setActive(true);
		
		// Update customer
		customer = customerService.updateCustomer(customer);

		return ResponseEntity.ok(new CartStatusResponse(customer.getCustomerCart()));
	}
	
	@PutMapping("/{userID}/cart/checkout")
	@PreAuthorize("hasRole('ROLE_ADMIN') || hasRole('ROLE_USER')")
	public ResponseEntity<?> checkoutUserCartByID(@PathVariable Long userID) throws NoRecordsFoundException, CustomerNotFoundException {
		checkUserAccess(userID);
		
		Customer customer = customerService.getCustomerByID(userID).orElseThrow(() -> {
			return new NoRecordsFoundException("Customer with ID: " + userID + " not found");
		});
		
		CustomerCart cart = customer.getCustomerCart();
		System.out.println("Pre checkout cart items: " + cart.getCartItems());
		cart.getCartItems().clear();
		cart.setActive(false);
		System.out.println("Post checkout cart items: " + cart.getCartItems());
		
		// Update customer
		customer = customerService.updateCustomer(customer);
		System.out.println("Post update customer: " + customer.getCustomerCart().getCartItems());

		return ResponseEntity.ok(new CartStatusResponse(customer.getCustomerCart()));
	}
}
