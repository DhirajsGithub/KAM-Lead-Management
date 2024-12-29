package com.udaan.kam.kam_lead_management.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.udaan.kam.kam_lead_management.entity.Contact;
import com.udaan.kam.kam_lead_management.entity.Restaurant;
import com.udaan.kam.kam_lead_management.service.ContactService;
import com.udaan.kam.kam_lead_management.service.RestaurantService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/restaurant")
public class RestaurantController {

    @Autowired
    private RestaurantService restaurantService;
    
    @Autowired
    private ContactService contactService;

    // Create a new Restaurant
    @PostMapping
    public ResponseEntity<Restaurant> createRestaurant(@Valid @RequestBody Restaurant restaurant) {
        Restaurant createdRestaurant = restaurantService.createRestaurant(restaurant);
        return ResponseEntity.ok(createdRestaurant);
    }

    // Get Restaurant by ID
    @GetMapping("/{id}")
    public ResponseEntity<Restaurant> getRestaurantById(@PathVariable Integer id) {
        // Directly call service, which throws exception if not found
        Restaurant restaurant = restaurantService.getRestaurantById(id);

        // Fetch the contacts associated with the Restaurant
        List<Contact> contacts = contactService.getContactsByRestaurantId(id);
        restaurant.setContacts(contacts);  // Associate contacts with the restaurant

        return ResponseEntity.ok(restaurant);
    }

    // Get All Restaurants
    @GetMapping
    public ResponseEntity<Page<Restaurant>> getRestaurants(
            @RequestParam(required = false) Restaurant.LeadStatus leadStatus,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<Restaurant> restaurants = restaurantService.getFilteredRestaurants(leadStatus, city, search, page, size);
        return ResponseEntity.ok(restaurants);
    }

    // Update a Restaurant
    @PutMapping("/{id}")
    public ResponseEntity<Restaurant> updateRestaurant(@PathVariable Integer id, @Valid @RequestBody Restaurant restaurant) {
        Restaurant updatedRestaurant = restaurantService.updateRestaurant(id, restaurant);
        return ResponseEntity.ok(updatedRestaurant);
    }

    // Delete a Restaurant
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRestaurant(@PathVariable Integer id) {
        restaurantService.deleteRestaurant(id);
        return ResponseEntity.noContent().build();
    }

    // Map Contact to Restaurant
    @PostMapping("/{restaurantId}/contacts/{contactId}")
    public ResponseEntity<Void> mapContactToRestaurant(@PathVariable Integer restaurantId, @PathVariable Integer contactId) {
        restaurantService.mapContactToRestaurant(restaurantId, contactId);
        return ResponseEntity.ok().build();
    }

    // Map Interaction to Restaurant
    @PostMapping("/{restaurantId}/interactions/{interactionId}")
    public ResponseEntity<Void> mapInteractionToRestaurant(@PathVariable Integer restaurantId, @PathVariable Integer interactionId) {
        restaurantService.mapInteractionToRestaurant(restaurantId, interactionId);
        return ResponseEntity.ok().build();
    }

    // Map Order to Restaurant
    @PostMapping("/{restaurantId}/orders/{orderId}")
    public ResponseEntity<Void> mapOrderToRestaurant(@PathVariable Integer restaurantId, @PathVariable Integer orderId) {
        restaurantService.mapOrderToRestaurant(restaurantId, orderId);
        return ResponseEntity.ok().build();
    }

    // Map Call Schedule to Restaurant
    @PostMapping("/{restaurantId}/call-schedules/{scheduleId}")
    public ResponseEntity<Void> mapCallScheduleToRestaurant(@PathVariable Integer restaurantId, @PathVariable Integer scheduleId) {
        restaurantService.mapCallScheduleToRestaurant(restaurantId, scheduleId);
        return ResponseEntity.ok().build();
    }

    // Map Performance Metric to Restaurant
    @PostMapping("/{restaurantId}/metrics/{metricId}")
    public ResponseEntity<Void> mapMetricToRestaurant(@PathVariable Integer restaurantId, @PathVariable Integer metricId) {
        restaurantService.mapMetricToRestaurant(restaurantId, metricId);
        return ResponseEntity.ok().build();
    }
}
