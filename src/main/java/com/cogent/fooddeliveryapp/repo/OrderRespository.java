package com.cogent.fooddeliveryapp.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cogent.fooddeliveryapp.dto.Order;

/**
 * OrderRespository
 *
 * @author bryan
 * @date Apr 7, 2022-6:49:07 PM
 */
@Repository
public interface OrderRespository extends JpaRepository<Order, Long> {

}
