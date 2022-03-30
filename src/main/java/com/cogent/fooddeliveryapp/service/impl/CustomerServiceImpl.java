package com.cogent.fooddeliveryapp.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;

import com.cogent.fooddeliveryapp.dto.Customer;
import com.cogent.fooddeliveryapp.exceptions.CustomerNotFoundException;
import com.cogent.fooddeliveryapp.repo.CustomerRepository;
import com.cogent.fooddeliveryapp.service.CustomerService;

/**
 * CustomerServiceImpl
 *
 * @author bryan
 * @date Feb 18, 2022-3:46:16 PM
 */
@Service
public class CustomerServiceImpl implements CustomerService {
	@Autowired
	private CustomerRepository repo;

	@Override
	public Customer addCustomer(Customer customer) {
		return repo.save(customer);
	}

	@Override
	public Optional<Customer> getCustomerByID(Long id) {
		return repo.findById(id);
	}
	
	@Override
	public Optional<Customer> getCustomerByEmail(String emailAddress) {
		return repo.findByEmail(emailAddress);
	}

	@Override
	public List<Customer> getAllCustomers() {
		return repo.findAll();
	}

	@Override
	public boolean deleteCustomerByID(Long id) {
		Customer customerRef = repo.getById(id);
		repo.delete(customerRef);
		return true;
	}

	@Override
	public Customer updateCustomer(Customer customer) throws CustomerNotFoundException {
		if (repo.existsById(customer.getId())) {
			return repo.save(customer);
		} else {
			throw new CustomerNotFoundException("Customer with id: " + customer.getId() + " not found");
		}
	}

	@Override
	public List<Customer> getAllCustomersAscOrder() {
		return repo.findAll(Sort.by(Order.asc("id")));
	}

	@Override
	public List<Customer> getAllCustomersDescOrder() {
		return repo.findAll(Sort.by(Order.desc("id")));
	}

	@Override
	public boolean existsByID(Long id) {
		return repo.existsById(id);
	}

	@Override
	public boolean existsByEmail(String emailAddress) {
		return repo.existsByEmail(emailAddress);
	}
}
