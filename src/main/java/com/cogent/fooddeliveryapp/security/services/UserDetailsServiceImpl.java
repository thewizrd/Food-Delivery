package com.cogent.fooddeliveryapp.security.services;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.cogent.fooddeliveryapp.dto.User;
import com.cogent.fooddeliveryapp.repo.UserRepository;

/**
 * UserDetailsServiceImpl
 *
 * @author bryan
 * @date Feb 23, 2022-10:50:57 AM
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	@Autowired
	private UserRepository userRepo;

	@Transactional // NOTE: fixes issue with lazy loading user roles
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepo.findByEmail(username).orElseThrow(() -> {
			return new UsernameNotFoundException("User with username: " + username + " not found");
		});
		
		return UserDetailsImpl.build(user);
	}
}
