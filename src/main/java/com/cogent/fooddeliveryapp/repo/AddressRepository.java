package com.cogent.fooddeliveryapp.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cogent.fooddeliveryapp.dto.Address;

/**
 * AddressRepository
 *
 * @author bryan
 * @date Feb 18, 2022-3:43:16 PM
 */
@Repository
public interface AddressRepository extends JpaRepository<Address, Integer> {

}
