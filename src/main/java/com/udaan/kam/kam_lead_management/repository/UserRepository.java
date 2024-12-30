package com.udaan.kam.kam_lead_management.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.udaan.kam.kam_lead_management.entity.User;


public interface UserRepository extends JpaRepository<User, Integer> {
	Optional<User> findByUsername(String username);
}
