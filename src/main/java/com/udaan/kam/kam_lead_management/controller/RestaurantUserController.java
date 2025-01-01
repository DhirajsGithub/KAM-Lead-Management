package com.udaan.kam.kam_lead_management.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.udaan.kam.kam_lead_management.entity.RestaurantUser;
import com.udaan.kam.kam_lead_management.service.RestaurantUserService;

@RestController
@RequestMapping("/api/restaurant-users")
public class RestaurantUserController {
	

    private final RestaurantUserService restaurantUserService;

    @Autowired
    public RestaurantUserController(RestaurantUserService restaurantUserService) {
        this.restaurantUserService = restaurantUserService;
    }

    // Add a user to a restaurant
    @PostMapping
    public ResponseEntity<String> addUserToRestaurant(@RequestBody RestaurantUser restaurantUser) {
        RestaurantUser createdRestaurantUser = restaurantUserService.addUserToRestaurant(restaurantUser);
        return ResponseEntity.status(201).body("Added Successfully");
    }

    // Delete a user-restaurant relationship
    @DeleteMapping
    public ResponseEntity<Void> deleteUserRestaurant(@RequestParam Integer restaurantId, @RequestParam Integer userId) {
        restaurantUserService.deleteUserRestaurant(restaurantId, userId);
        return ResponseEntity.noContent().build();
    }
}
