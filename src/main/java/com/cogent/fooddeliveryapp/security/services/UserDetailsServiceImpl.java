package com.cogent.fooddeliveryapp.security.services;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.cogent.fooddeliveryapp.dto.Customer;
import com.cogent.fooddeliveryapp.repo.CustomerRepository;

/**
 * UserDetailsServiceImpl
 *
 * @author bryan
 * @date Feb 23, 2022-10:50:57 AM
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	@Autowired
	private CustomerRepository customerRepo;

	@Transactional // NOTE: fixes issue with lazy loading user roles
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Customer user = customerRepo.findCustomerByEmail(username).orElseThrow(() -> {
			return new UsernameNotFoundException("User with username: " + username + " not found");
		});
		
		return UserDetailsImpl.build(user);
	}
}
