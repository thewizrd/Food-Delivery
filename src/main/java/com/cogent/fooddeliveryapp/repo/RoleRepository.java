package com.cogent.fooddeliveryapp.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cogent.fooddeliveryapp.dto.Role;
import com.cogent.fooddeliveryapp.enums.UserRoles;

/**
 * RoleRepository
 *
 * @author bryan
 * @date Feb 18, 2022-4:04:48 PM
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
	Optional<Role> findRoleByRoleName(UserRoles roleName);
}
