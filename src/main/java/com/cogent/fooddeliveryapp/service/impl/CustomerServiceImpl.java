package com.cogent.fooddeliveryapp.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.cogent.fooddeliveryapp.dto.Customer;
import com.cogent.fooddeliveryapp.dto.CustomerCart;
import com.cogent.fooddeliveryapp.dto.Order;
import com.cogent.fooddeliveryapp.dto.OrderItem;
import com.cogent.fooddeliveryapp.exceptions.CustomerNotFoundException;
import com.cogent.fooddeliveryapp.exceptions.InvalidRequestException;
import com.cogent.fooddeliveryapp.repo.CustomerRepository;
import com.cogent.fooddeliveryapp.repo.OrderRespository;
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
	@Autowired
	private OrderRespository orderRepo;

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
		return repo.findAll(Sort.by(Sort.Order.asc("id")));
	}

	@Override
	public List<Customer> getAllCustomersDescOrder() {
		return repo.findAll(Sort.by(Sort.Order.desc("id")));
	}

	@Override
	public boolean existsByID(Long id) {
		return repo.existsById(id);
	}

	@Override
	public boolean existsByEmail(String emailAddress) {
		return repo.existsByEmail(emailAddress);
	}

	@Transactional
	@Override
	public Order checkoutOrder(Customer customer) throws InvalidRequestException {
		CustomerCart cart = customer.getCustomerCart();
		
		// Create order and add to database
		Order order = new Order(customer);
		order.setOrderDate(LocalDateTime.now());
		order.setAddress(customer.getAddresses().stream().findFirst().orElseThrow(() -> {
			return new InvalidRequestException("No address found for customer");
		}));

		order.setOrderItems(cart.getCartItems().stream().map((ci) -> {
			return new OrderItem(ci.getFood(), order);
		}).collect(Collectors.toSet()));

		order.setOrderTotal(cart.getTotal());
		
		Order updateOrder = orderRepo.save(order);

		// Then clear cart
		cart.getCartItems().clear();
		cart.setActive(false);

		// Update customer
		repo.save(customer);
		
		return updateOrder;
	}
}
