package com.udaan.kam.kam_lead_management.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.udaan.kam.kam_lead_management.entity.RestaurantUser;

public interface RestaurantUserRepository extends JpaRepository<RestaurantUser, Integer> {
    List<RestaurantUser> findByRestaurantId(Integer restaurantId);
    List<RestaurantUser> findByUserId(Integer userId);
    Optional<RestaurantUser> findByRestaurantIdAndUserId(Integer restaurantId, Integer userId);
    
}
