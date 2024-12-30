package com.udaan.kam.kam_lead_management.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.udaan.kam.kam_lead_management.entity.CallSchedule;
import com.udaan.kam.kam_lead_management.entity.Contact;
import com.udaan.kam.kam_lead_management.entity.Interaction;
import com.udaan.kam.kam_lead_management.entity.Order;
import com.udaan.kam.kam_lead_management.entity.PerformanceMetric;
import com.udaan.kam.kam_lead_management.entity.Restaurant;
import com.udaan.kam.kam_lead_management.entity.RestaurantUser;
import com.udaan.kam.kam_lead_management.entity.User;
import com.udaan.kam.kam_lead_management.exception.RestaurantNotFoundException;
import com.udaan.kam.kam_lead_management.exception.UnauthorizedAccessException;
import com.udaan.kam.kam_lead_management.repository.ContactRepository;
import com.udaan.kam.kam_lead_management.repository.InteractionRepository;
import com.udaan.kam.kam_lead_management.repository.OrderRepository;
import com.udaan.kam.kam_lead_management.repository.PerformanceMetricRepository;
import com.udaan.kam.kam_lead_management.repository.RestaurantRepository;
import com.udaan.kam.kam_lead_management.repository.RestaurantUserRepository;
import com.udaan.kam.kam_lead_management.repository.ScheduleRepository;
import com.udaan.kam.kam_lead_management.repository.UserRepository;

@Service
public class RestaurantService {

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private InteractionRepository interactionRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ScheduleRepository callScheduleRepository;

    @Autowired
    private PerformanceMetricRepository performanceMetricRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private RestaurantUserRepository restaurantUserRepository;

    // Create a new Restaurant
    public Restaurant createRestaurant(Restaurant restaurant) {
        return restaurantRepository.save(restaurant);
    }

    // Get a Restaurant by ID
    public Restaurant getRestaurantById(Integer id) {
        Optional<Restaurant> restaurant = restaurantRepository.findById(id);
        return restaurant.orElseThrow(() -> new RestaurantNotFoundException("Restaurant not found with ID: " + id));
    }

//    // Get All Restaurants with filters
//    public List<Restaurant> getAllRestaurants(String leadStatus, String city) {
//        if (leadStatus != null && city != null) {
//            return restaurantRepository.findByLeadStatusAndCity(leadStatus, city);
//        }
//        return restaurantRepository.findAll();
//    }
    
    // Get Filtered Resturant base on leadStatus, city, search and page
    public Page<Restaurant> getFilteredRestaurants(Restaurant.LeadStatus leadStatus, String city, String search, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return restaurantRepository.findByFiltersAndSearch(leadStatus, city, search, pageable);
    }
    
    public Page<Restaurant> getAssignedRestaurants(Integer userId, Restaurant.LeadStatus leadStatus, String city, String search, int page, int size) {
        // Find the restaurants assigned to the user
        List<RestaurantUser> assignedRestaurants = restaurantUserRepository.findByUserId(userId);
        
        // Extract restaurant IDs from the assigned restaurants
        List<Integer> assignedRestaurantIds = assignedRestaurants.stream()
                                                                .map(ru -> ru.getRestaurant().getId())
                                                                .collect(Collectors.toList());

        // Create Pageable object
        Pageable pageable = PageRequest.of(page, size);

        // Use the restaurant IDs to filter restaurants
        return restaurantRepository.findByIdInAndFiltersAndSearch(assignedRestaurantIds, leadStatus, city, search, pageable);
    }
    
    
    public boolean isUserAssignedToRestaurant(Integer userId, Integer restaurantId) {
        return restaurantUserRepository.existsByRestaurantIdAndUserId(restaurantId, userId);
    }

    // Update a Restaurant
    public Restaurant updateRestaurant(Integer userId, Integer id, Restaurant restaurant) {
        // Check if the user is authorized (Admin or assigned Manager)
        if (isUserAdmin(userId) || isUserAssignedToRestaurant(userId, id)) {
            return restaurantRepository.save(restaurant);  // Save the updated restaurant
        } else {
            throw new UnauthorizedAccessException("You are not authorized to update this restaurant.");
        }
    }

    // Delete a Restaurant
    public void deleteRestaurant(Integer userId, Integer id) {
        // Check if the user is authorized (Admin or assigned Manager)
        if (isUserAdmin(userId) || isUserAssignedToRestaurant(userId, id)) {
            restaurantRepository.deleteById(id);
        } else {
            throw new UnauthorizedAccessException("You are not authorized to delete this restaurant.");
        }
    }

    // Helper method to check if user is admin
    public boolean isUserAdmin(Integer userId) {
        Optional<User> user = userRepository.findById(userId);
        return user.isPresent() && user.get().getRole().toString().equals("ADMIN");
    }

//    // Update a Restaurant
//    public Restaurant updateRestaurant(Integer id, Restaurant restaurant) {
//        if (restaurantRepository.existsById(id)) {
//            restaurant.setId(id);
//            return restaurantRepository.save(restaurant);
//        }
//        throw new RestaurantNotFoundException("Restaurant not found with ID: " + id);
//    }
//
//    // Delete a Restaurant
//    public boolean deleteRestaurant(Integer id) {
//        if (restaurantRepository.existsById(id)) {
//            restaurantRepository.deleteById(id);
//            return true;
//        }
//        throw new RestaurantNotFoundException("Restaurant not found with ID: " + id);
//    }
    
    public List<Restaurant> getRestaurantsByUser(Integer userId) {
        return restaurantRepository.findRestaurantsByUserId(userId);
    }

    // Map Contact to Restaurant
    public boolean mapContactToRestaurant(Integer restaurantId, Integer contactId) {
        Optional<Restaurant> restaurant = restaurantRepository.findById(restaurantId);
        Optional<Contact> contact = contactRepository.findById(contactId);
        if (restaurant.isPresent() && contact.isPresent()) {
            restaurant.get().setPrimaryContact(contact.get());
            restaurantRepository.save(restaurant.get());
            return true;
        }
        throw new RestaurantNotFoundException("Restaurant or Contact not found.");
    }

    // Map Interaction to Restaurant
    public boolean mapInteractionToRestaurant(Integer restaurantId, Integer interactionId) {
        Optional<Restaurant> restaurant = restaurantRepository.findById(restaurantId);
        Optional<Interaction> interaction = interactionRepository.findById(interactionId);
        if (restaurant.isPresent() && interaction.isPresent()) {
            restaurant.get().setLatestInteraction(interaction.get());
            restaurantRepository.save(restaurant.get());
            return true;
        }
        throw new RestaurantNotFoundException("Restaurant or Interaction not found.");
    }

    // Map Order to Restaurant
    public boolean mapOrderToRestaurant(Integer restaurantId, Integer orderId) {
        Optional<Restaurant> restaurant = restaurantRepository.findById(restaurantId);
        Optional<Order> order = orderRepository.findById(orderId);
        if (restaurant.isPresent() && order.isPresent()) {
            restaurant.get().setLatestOrder(order.get());
            restaurantRepository.save(restaurant.get());
            return true;
        }
        throw new RestaurantNotFoundException("Restaurant or Order not found.");
    }

    // Map Call Schedule to Restaurant
    public boolean mapCallScheduleToRestaurant(Integer restaurantId, Integer scheduleId) {
        Optional<Restaurant> restaurant = restaurantRepository.findById(restaurantId);
        Optional<CallSchedule> schedule = callScheduleRepository.findById(scheduleId);
        if (restaurant.isPresent() && schedule.isPresent()) {
            restaurant.get().setCurrentSchedule(schedule.get());
            restaurantRepository.save(restaurant.get());
            return true;
        }
        throw new RestaurantNotFoundException("Restaurant or Call Schedule not found.");
    }

    // Map Performance Metric to Restaurant
    public boolean mapMetricToRestaurant(Integer restaurantId, Integer metricId) {
        Optional<Restaurant> restaurant = restaurantRepository.findById(restaurantId);
        Optional<PerformanceMetric> metric = performanceMetricRepository.findById(metricId);
        if (restaurant.isPresent() && metric.isPresent()) {
            restaurant.get().setLatestMetric(metric.get());
            restaurantRepository.save(restaurant.get());
            return true;
        }
        throw new RestaurantNotFoundException("Restaurant or Metric not found.");
    }
}
