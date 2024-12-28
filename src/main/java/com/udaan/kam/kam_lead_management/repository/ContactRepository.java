package com.udaan.kam.kam_lead_management.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.udaan.kam.kam_lead_management.entity.Contact;

public interface ContactRepository extends JpaRepository<Contact, Integer> {
	List<Contact> findByRestaurantId(Integer restaurantId);
}
