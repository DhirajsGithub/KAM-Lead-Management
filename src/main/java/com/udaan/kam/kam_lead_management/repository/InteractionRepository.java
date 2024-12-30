package com.udaan.kam.kam_lead_management.repository;

import com.udaan.kam.kam_lead_management.entity.Interaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InteractionRepository extends JpaRepository<Interaction, Integer> {
    List<Interaction> findByRestaurantId(Integer restaurantId);
}
