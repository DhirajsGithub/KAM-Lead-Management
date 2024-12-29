package com.udaan.kam.kam_lead_management.service;

import java.util.Optional;

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
import com.udaan.kam.kam_lead_management.exception.RestaurantNotFoundException;
import com.udaan.kam.kam_lead_management.repository.ContactRepository;
import com.udaan.kam.kam_lead_management.repository.InteractionRepository;
import com.udaan.kam.kam_lead_management.repository.OrderRepository;
import com.udaan.kam.kam_lead_management.repository.PerformanceMetricRepository;
import com.udaan.kam.kam_lead_management.repository.RestaurantRepository;
import com.udaan.kam.kam_lead_management.repository.ScheduleRepository;

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

    // Update a Restaurant
    public Restaurant updateRestaurant(Integer id, Restaurant restaurant) {
        if (restaurantRepository.existsById(id)) {
            restaurant.setId(id);
            return restaurantRepository.save(restaurant);
        }
        throw new RestaurantNotFoundException("Restaurant not found with ID: " + id);
    }

    // Delete a Restaurant
    public boolean deleteRestaurant(Integer id) {
        if (restaurantRepository.existsById(id)) {
            restaurantRepository.deleteById(id);
            return true;
        }
        throw new RestaurantNotFoundException("Restaurant not found with ID: " + id);
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
