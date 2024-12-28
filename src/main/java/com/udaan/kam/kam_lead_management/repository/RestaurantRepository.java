package com.udaan.kam.kam_lead_management.repository;

import com.udaan.kam.kam_lead_management.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RestaurantRepository extends JpaRepository<Restaurant, Integer> {
    List<Restaurant> findByLeadStatusAndCity(String leadStatus, String city);
}
