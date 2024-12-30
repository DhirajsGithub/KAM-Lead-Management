package com.udaan.kam.kam_lead_management.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.udaan.kam.kam_lead_management.entity.CallSchedule;
import com.udaan.kam.kam_lead_management.entity.Contact;
import com.udaan.kam.kam_lead_management.entity.Restaurant;
import com.udaan.kam.kam_lead_management.exception.UnauthorizedAccessException;
import com.udaan.kam.kam_lead_management.security.UserDetailsImpl;
import com.udaan.kam.kam_lead_management.service.CallScheduleService;
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
    
    @Autowired
    private CallScheduleService callScheduleService;

    // Create a new Restaurant (Admin only)
    @PostMapping
    public ResponseEntity<Restaurant> createRestaurant(@Valid @RequestBody Restaurant restaurant) {
        Restaurant createdRestaurant = restaurantService.createRestaurant(restaurant);
        return ResponseEntity.ok(createdRestaurant);
    }

    // Get Restaurant by ID (Accessible to Admin or Manager assigned to the restaurant)
    @GetMapping("/{id}")
    public ResponseEntity<Restaurant> getRestaurantById(@PathVariable Integer id,
                                                        @AuthenticationPrincipal UserDetails currentUser) {
    	 UserDetailsImpl userDetailsImpl = (UserDetailsImpl) currentUser;  
    	 Integer userId = userDetailsImpl.getUserId(); 
        Restaurant restaurant = restaurantService.getRestaurantById(id);

        // Check if the user is an admin or assigned to this restaurant
        if (restaurantService.isUserAdmin(userId) || restaurantService.isUserAssignedToRestaurant(userId, id)) {
            // Fetch the contacts associated with the Restaurant
            List<Contact> contacts = contactService.getContactsByRestaurantId(id, userId);
            List<CallSchedule> callSchedules = callScheduleService.getCallSchedulesByRestaurantId(id, userId);
            restaurant.setCallSchedules(callSchedules);
            restaurant.setContacts(contacts);  

            return ResponseEntity.ok(restaurant);
        } else {
            throw new UnauthorizedAccessException("You are not authorized to access this restaurant.");
        }
    }

    // Get All Restaurants (Admin can see all, Managers can only see assigned ones)
    @GetMapping
    public ResponseEntity<Page<Restaurant>> getRestaurants(@RequestParam(required = false) Restaurant.LeadStatus leadStatus,
                                                            @RequestParam(required = false) String city,
                                                            @RequestParam(required = false) String search,
                                                            @RequestParam(defaultValue = "0") int page,
                                                            @RequestParam(defaultValue = "10") int size,
                                                            @AuthenticationPrincipal UserDetails currentUser) {
    	 UserDetailsImpl userDetailsImpl = (UserDetailsImpl) currentUser;  // Casting to your custom implementation
    	 Integer userId = userDetailsImpl.getUserId(); 
    		System.out.println("userId "+restaurantService.isUserAdmin(userId));
        if (restaurantService.isUserAdmin(userId)) {
            // Admin can access all restaurants
        
            Page<Restaurant> restaurants = restaurantService.getFilteredRestaurants(leadStatus, city, search, page, size);
            return ResponseEntity.ok(restaurants);
        } else {
            // Managers can only see assigned restaurants
            Page<Restaurant> restaurants = restaurantService.getAssignedRestaurants(userId, leadStatus, city, search, page, size);
            return ResponseEntity.ok(restaurants);
        }
    }

    // Update a Restaurant (Manager can only update assigned, Admin can update any)
    @PutMapping("/{id}")
    public ResponseEntity<Restaurant> updateRestaurant(@PathVariable Integer id,
                                                       @Valid @RequestBody Restaurant restaurant,
                                                       @AuthenticationPrincipal UserDetails currentUser) {
    	 UserDetailsImpl userDetailsImpl = (UserDetailsImpl) currentUser;  // Casting to your custom implementation
    	 Integer userId = userDetailsImpl.getUserId(); 
        Restaurant updatedRestaurant = restaurantService.updateRestaurant(userId, id, restaurant);
        return ResponseEntity.ok(updatedRestaurant);
    }

    // Delete a Restaurant (Manager can only delete assigned, Admin can delete any)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRestaurant(@PathVariable Integer id,
                                                 @AuthenticationPrincipal UserDetails currentUser) {
    	 UserDetailsImpl userDetailsImpl = (UserDetailsImpl) currentUser;  // Casting to your custom implementation
    	 Integer userId = userDetailsImpl.getUserId(); 
        restaurantService.deleteRestaurant(userId, id);
        return ResponseEntity.noContent().build();
    }


}
