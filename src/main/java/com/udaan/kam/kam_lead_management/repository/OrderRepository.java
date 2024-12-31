package com.udaan.kam.kam_lead_management.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.udaan.kam.kam_lead_management.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Integer> {
	 List<Order> findByRestaurantId(Integer restaurantId);
}
