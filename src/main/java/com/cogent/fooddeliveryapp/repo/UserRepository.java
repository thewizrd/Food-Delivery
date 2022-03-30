package com.cogent.fooddeliveryapp.repo;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cogent.fooddeliveryapp.dto.Customer;
import com.cogent.fooddeliveryapp.dto.User;

/**
 * CustomerRepository
 *
 * @author bryan
 * @date Feb 18, 2022-3:41:53 PM
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	@Transactional // make transactional -- nested object relationships
	@Override
	<S extends User> S save(S entity);
	
	public Optional<User> findByEmail(String emailAddress);
	public boolean existsByEmail(String emailAddress);
}
