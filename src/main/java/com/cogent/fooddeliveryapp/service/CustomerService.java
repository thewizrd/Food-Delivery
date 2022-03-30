package com.cogent.fooddeliveryapp.service;

import java.util.List;
import java.util.Optional;

import com.cogent.fooddeliveryapp.dto.Customer;
import com.cogent.fooddeliveryapp.exceptions.CustomerNotFoundException;

/**
 * CustomerService
 *
 * @author bryan
 * @date Feb 18, 2022-3:45:10 PM
 */
public interface CustomerService {
	Customer addCustomer(Customer customer);
	Optional<Customer> getCustomerByID(Long id);
	Optional<Customer> getCustomerByEmail(String emailAddress);
	List<Customer> getAllCustomers();
	boolean deleteCustomerByID(Long id);
	Customer updateCustomer(Customer customer) throws CustomerNotFoundException;
	List<Customer> getAllCustomersAscOrder();
	List<Customer> getAllCustomersDescOrder();
	boolean existsByID(Long id);
	boolean existsByEmail(String emailAddress);
}
