package com.udaan.kam.kam_lead_management.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.udaan.kam.kam_lead_management.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Integer> {

}
