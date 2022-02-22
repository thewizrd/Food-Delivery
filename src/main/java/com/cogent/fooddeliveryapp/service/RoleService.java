package com.cogent.fooddeliveryapp.service;

import java.util.List;
import java.util.Optional;

import com.cogent.fooddeliveryapp.dto.Role;
import com.cogent.fooddeliveryapp.enums.UserRoles;

/**
 * RoleService
 *
 * @author bryan
 * @date Feb 18, 2022-4:35:05 PM
 */
public interface RoleService {
	Optional<Role> getRoleByID(int roleID);
	List<Role> getAllRoles();
	Optional<Role> getRoleByName(UserRoles roleName);
}
