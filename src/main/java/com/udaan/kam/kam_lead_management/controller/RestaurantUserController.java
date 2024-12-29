package com.udaan.kam.kam_lead_management.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
    public ResponseEntity<RestaurantUser> addUserToRestaurant(@RequestBody RestaurantUser restaurantUser) {
        RestaurantUser createdRestaurantUser = restaurantUserService.addUserToRestaurant(restaurantUser);
        return ResponseEntity.status(201).body(createdRestaurantUser);
    }

    // Get all users associated with a restaurant
    @GetMapping("/restaurant/{restaurantId}")
    public ResponseEntity<List<RestaurantUser>> getUsersByRestaurant(@PathVariable Integer restaurantId) {
        List<RestaurantUser> users = restaurantUserService.getUsersByRestaurant(restaurantId);
        return ResponseEntity.ok(users);
    }

    // Get all restaurants associated with a user
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<RestaurantUser>> getRestaurantsByUser(@PathVariable Integer userId) {
        List<RestaurantUser> restaurants = restaurantUserService.getRestaurantsByUser(userId);
        return ResponseEntity.ok(restaurants);
    }

    // Delete a user-restaurant relationship
    @DeleteMapping
    public ResponseEntity<Void> deleteUserRestaurant(@RequestParam Integer restaurantId, @RequestParam Integer userId) {
        restaurantUserService.deleteUserRestaurant(restaurantId, userId);
        return ResponseEntity.noContent().build();
    }
}
