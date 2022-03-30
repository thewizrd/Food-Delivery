package com.cogent.fooddeliveryapp.service.impl;

import java.util.List;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cogent.fooddeliveryapp.dto.Role;
import com.cogent.fooddeliveryapp.enums.UserRoles;
import com.cogent.fooddeliveryapp.repo.RoleRepository;
import com.cogent.fooddeliveryapp.service.RoleService;

/**
 * RoleServiceImpl
 *
 * @author bryan
 * @date Feb 18, 2022-4:36:34 PM
 */
@Service
public class RoleServiceImpl implements RoleService {
	@Autowired
	private RoleRepository repo;

	@Override
	public Optional<Role> getRoleByID(Long roleID) {
		return repo.findById(roleID);
	}

	@Override
	public List<Role> getAllRoles() {
		return repo.findAll();
	}

	@Override
	public Optional<Role> getRoleByName(UserRoles roleName) {
		return repo.findRoleByRoleName(roleName);
	}
}
