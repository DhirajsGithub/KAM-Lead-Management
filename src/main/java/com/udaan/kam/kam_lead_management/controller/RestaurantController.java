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

import com.udaan.kam.kam_lead_management.DTO.InteractionDTO;
import com.udaan.kam.kam_lead_management.DTO.RestaurantDTO;
import com.udaan.kam.kam_lead_management.DTO.RestaurantDetailDTO;
import com.udaan.kam.kam_lead_management.entity.CallSchedule;
import com.udaan.kam.kam_lead_management.entity.Contact;
import com.udaan.kam.kam_lead_management.entity.Order;
import com.udaan.kam.kam_lead_management.entity.PerformanceMetric;
import com.udaan.kam.kam_lead_management.entity.Restaurant;
import com.udaan.kam.kam_lead_management.exception.UnauthorizedAccessException;
import com.udaan.kam.kam_lead_management.security.UserDetailsImpl;
import com.udaan.kam.kam_lead_management.service.CallScheduleService;
import com.udaan.kam.kam_lead_management.service.ContactService;
import com.udaan.kam.kam_lead_management.service.InteractionService;
import com.udaan.kam.kam_lead_management.service.OrderService;
import com.udaan.kam.kam_lead_management.service.PerformanceMetricService;
import com.udaan.kam.kam_lead_management.service.RestaurantService;
import com.udaan.kam.kam_lead_management.util.DTOConverterUtil;
import com.udaan.kam.kam_lead_management.util.PermissionUtils;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/restaurant")
public class RestaurantController {

	@Autowired
	private RestaurantService restaurantService;

	@Autowired
	private InteractionService interactionsService;

	@Autowired
	private OrderService orderService;
	
	
	@Autowired
	private ContactService contactService;

	@Autowired
	private CallScheduleService callScheduleService;

	@Autowired
	private PermissionUtils permissionUtils;
	
	 @Autowired
	 private DTOConverterUtil dtoConverter;
	 
	 @Autowired
	 private PerformanceMetricService performanceMetricService;

	@PostMapping
	// Create a Restaurant
	public ResponseEntity<RestaurantDTO> createRestaurant(@Valid @RequestBody Restaurant restaurant) {
		RestaurantDTO createdRestaurant = restaurantService.createRestaurant(restaurant);
		return ResponseEntity.ok(createdRestaurant);
	}

	@GetMapping("/{restaurant_id}")
	public ResponseEntity<RestaurantDetailDTO> getRestaurantById(@PathVariable Integer restaurant_id,
	        @AuthenticationPrincipal UserDetails currentUser) {
	    UserDetailsImpl userDetailsImpl = (UserDetailsImpl) currentUser;
	    Integer userId = userDetailsImpl.getUserId();
	    Restaurant restaurant = restaurantService.getRestaurantById(restaurant_id);

	    if (permissionUtils.isAdminOrAssignedManager(userId, restaurant_id)) {
	        List<Contact> contacts = contactService.getContactsByRestaurantId(restaurant_id, userId);
	        List<CallSchedule> callSchedules = callScheduleService.getCallSchedulesByRestaurantId(restaurant_id, userId);
	        List<InteractionDTO> interactions = interactionsService.getInteractionsByRestaurantId(restaurant_id, userId);
	        List<Order> orders = orderService.getOrdersByRestaurantId(restaurant_id, userId);
	        List<PerformanceMetric> performanceMetrices = performanceMetricService.getMetricsByRestaurantId(userId, restaurant_id);
	        restaurant.setCallSchedules(callSchedules);
	        restaurant.setContacts(contacts);

	        RestaurantDetailDTO detailDTO = dtoConverter.convertToRestaurantDetailDTO(restaurant, contacts, callSchedules, interactions, orders, performanceMetrices);

	        return ResponseEntity.ok(detailDTO);
	    } else {
	        throw new UnauthorizedAccessException("You are not authorized to access this restaurant.");
	    }
	}

	// Get All Restaurants (Admin can see all, Managers can only see assigned ones)
	@GetMapping
	public ResponseEntity<Page<RestaurantDTO>> getRestaurants(
	        @RequestParam(required = false) Restaurant.LeadStatus leadStatus,
	        @RequestParam(required = false) String city,
	        @RequestParam(required = false) String search,
	        @RequestParam(defaultValue = "0") int page,
	        @RequestParam(defaultValue = "10") int size,
	        @AuthenticationPrincipal UserDetails currentUser) {
	    
	    UserDetailsImpl userDetailsImpl = (UserDetailsImpl) currentUser;
	    Integer userId = userDetailsImpl.getUserId();
	    
	    Page<RestaurantDTO> restaurants;
	    if (permissionUtils.isAdmin(userId)) {
	        restaurants = restaurantService.getFilteredRestaurants(leadStatus, city, search, page, size);
	    } else {
	        restaurants = restaurantService.getAssignedRestaurants(userId, leadStatus, city, search, page, size);
	    }
	    
	    return ResponseEntity.ok(restaurants);
	}

	@PutMapping("/{restaurant_id}")
	// Update a Restaurants (Admin or assigned Managers)
	public ResponseEntity<RestaurantDTO> updateRestaurant(@PathVariable Integer restaurant_id,
			@Valid @RequestBody Restaurant restaurant, @AuthenticationPrincipal UserDetails currentUser) {
		UserDetailsImpl userDetailsImpl = (UserDetailsImpl) currentUser;
		Integer userId = userDetailsImpl.getUserId();
		RestaurantDTO updatedRestaurant = restaurantService.updateRestaurant(userId, restaurant_id, restaurant);
		return ResponseEntity.ok(updatedRestaurant);
	}

	// Delete a Restaurants (Admin or assigned Managers)
	@DeleteMapping("/{restaurant_id}")
	public ResponseEntity<Void> deleteRestaurant(@PathVariable Integer restaurant_id,
			@AuthenticationPrincipal UserDetails currentUser) {
		UserDetailsImpl userDetailsImpl = (UserDetailsImpl) currentUser;
		Integer userId = userDetailsImpl.getUserId();
		restaurantService.deleteRestaurant(userId, restaurant_id);
		return ResponseEntity.noContent().build();
	}

}
