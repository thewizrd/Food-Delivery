package com.cogent.fooddeliveryapp.utils;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.cogent.fooddeliveryapp.dto.Role;
import com.cogent.fooddeliveryapp.dto.User;
import com.cogent.fooddeliveryapp.enums.UserRoles;
import com.cogent.fooddeliveryapp.exceptions.RoleNotFoundException;
import com.cogent.fooddeliveryapp.repo.RoleRepository;
import com.cogent.fooddeliveryapp.repo.UserRepository;

/**
 * DBUtils
 *
 * @author bryan
 * @date Mar 30, 2022-4:03:47 PM
 */
@Component
public class DBUtils {
	private static final String DEFAULT_ADMIN_USERNAME = "admin@admin.com";
	private static final String DEFAULT_ADMIN_PASSWORD = "secret@123";
	
	@Autowired
	private RoleRepository roleRepo;
	@Autowired
	private UserRepository userRepo;
	@Autowired
	private PasswordEncoder passwordEncoder;

	@PostConstruct
	private void initializeDB() {
		initializeRoles();
		initializeAdmin();
	}

	private void initializeRoles() {
		UserRoles[] roles = UserRoles.values();
		for (UserRoles role : roles) {
			Optional<Role> roleEntity = roleRepo.findRoleByRoleName(role);
			if (!roleEntity.isPresent()) {
				roleRepo.save(new Role(role));
			}
		}
	}
	
	private void initializeAdmin() {
		if (!userRepo.existsByEmail(DEFAULT_ADMIN_USERNAME)) {
			User admin = new User();
			admin.setName("Administrator");
			admin.setPassword(passwordEncoder.encode(DEFAULT_ADMIN_PASSWORD));
			admin.setEmail(DEFAULT_ADMIN_USERNAME);
			admin.setRoles(Collections.singleton(roleRepo.findRoleByRoleName(UserRoles.ROLE_ADMIN).orElseThrow(() -> {
				return new RoleNotFoundException("Role name: " + UserRoles.ROLE_ADMIN + " not found");
			})));
			admin.setDoj(LocalDate.now());
			
			userRepo.save(admin);
		}
	}
}
