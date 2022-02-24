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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cogent.fooddeliveryapp.dto.Address;
import com.cogent.fooddeliveryapp.dto.Customer;
import com.cogent.fooddeliveryapp.dto.Role;
import com.cogent.fooddeliveryapp.enums.UserRoles;
import com.cogent.fooddeliveryapp.exceptions.InvalidRequestException;
import com.cogent.fooddeliveryapp.exceptions.RoleNotFoundException;
import com.cogent.fooddeliveryapp.payload.request.AddressRequest;
import com.cogent.fooddeliveryapp.payload.request.CustomerRegistrationRequest;
import com.cogent.fooddeliveryapp.payload.request.LoginRequest;
import com.cogent.fooddeliveryapp.payload.response.ApiMessage;
import com.cogent.fooddeliveryapp.payload.response.CustomerResponse;
import com.cogent.fooddeliveryapp.payload.response.JwtResponse;
import com.cogent.fooddeliveryapp.security.jwt.JwtUtils;
import com.cogent.fooddeliveryapp.security.services.UserDetailsImpl;
import com.cogent.fooddeliveryapp.service.CustomerService;
import com.cogent.fooddeliveryapp.service.RoleService;

/**
 * AuthController
 *
 * @author bryan
 * @date Feb 24, 2022-11:18:19 AM
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {
	@Autowired
	private CustomerService customerService;
	@Autowired
	private RoleService roleService;
	@Autowired
	private Function<AddressRequest, Address> addressMapper;
	
	@Autowired
	private AuthenticationManager authManager;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private JwtUtils jwtUtils;
	
	@PostMapping("/register")
	public ResponseEntity<?> createUser(@Valid @RequestBody CustomerRegistrationRequest request) throws InvalidRequestException {
		if (!customerService.existsByEmail(request.getEmail())) {
			Customer customer = new Customer();
			customer.setEmail(request.getEmail());
			customer.setPassword(passwordEncoder.encode(request.getPassword())); // encode password
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
			
			return ResponseEntity.status(HttpStatus.CREATED).body(new CustomerResponse(created));
		} else {
			throw new InvalidRequestException("User with email address already exists");
		}
	}
	
	@PostMapping("/login")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest request) {
		Authentication authentication = authManager.authenticate(
				new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
		
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		String jwt = jwtUtils.generateToken(authentication);
		
		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
		
		List<String> roles = userDetails.getAuthorities().parallelStream()
				.map(authorities -> {
					return authorities.getAuthority();
				}).collect(Collectors.toList());
		
		return ResponseEntity.ok(new JwtResponse(jwt, userDetails.getId(), userDetails.getUsername(), roles));
	}
}
