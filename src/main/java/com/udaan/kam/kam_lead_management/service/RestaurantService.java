package com.udaan.kam.kam_lead_management.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.udaan.kam.kam_lead_management.DTO.RestaurantDTO;
import com.udaan.kam.kam_lead_management.entity.Restaurant;
import com.udaan.kam.kam_lead_management.entity.RestaurantUser;
import com.udaan.kam.kam_lead_management.exception.RestaurantNotFoundException;
import com.udaan.kam.kam_lead_management.exception.UnauthorizedAccessException;
import com.udaan.kam.kam_lead_management.repository.RestaurantRepository;
import com.udaan.kam.kam_lead_management.repository.RestaurantUserRepository;
import com.udaan.kam.kam_lead_management.util.DTOConverterUtil;
import com.udaan.kam.kam_lead_management.util.PermissionUtils;

@Service
public class RestaurantService {

    @Autowired
    private RestaurantRepository restaurantRepository;

    
    @Autowired
    private RestaurantUserRepository restaurantUserRepository;
    
    @Autowired
    private PermissionUtils permissionUtils;
    
	 @Autowired
	 private DTOConverterUtil dtoConverter;

    // Create a new Restaurant
    public RestaurantDTO createRestaurant(Restaurant restaurant) {
        Restaurant res =  restaurantRepository.save(restaurant);
        return dtoConverter.convertToRestaurantDTO(restaurant);
    }


	// Get a Restaurant by ID
    public Restaurant getRestaurantById(Integer id) {
        Optional<Restaurant> restaurant = restaurantRepository.findById(id);
        return restaurant.orElseThrow(() -> new RestaurantNotFoundException("Restaurant not found with ID: " + id));
    }

    // Get Filtered Resturant base on leadStatus, city, search and page
    public Page<RestaurantDTO> getFilteredRestaurants(Restaurant.LeadStatus leadStatus, String city, String search, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        
        Page<Restaurant> restaurantPage = restaurantRepository.findByFiltersAndSearch(leadStatus, city, search, pageable);
        
        return restaurantPage.map(this::createRestaurantDto);
    }
    
    public Page<RestaurantDTO> getAssignedRestaurants(Integer userId, Restaurant.LeadStatus leadStatus, String city, String search, int page, int size) {
        List<RestaurantUser> assignedRestaurants = restaurantUserRepository.findByUserId(userId);
        
        List<Integer> assignedRestaurantIds = assignedRestaurants.stream()
                                                                .map(ru -> ru.getRestaurant().getId())
                                                                .collect(Collectors.toList());

        Pageable pageable = PageRequest.of(page, size);

        Page<Restaurant> restaurantPage = restaurantRepository.findByIdInAndFiltersAndSearch(
            assignedRestaurantIds, 
            leadStatus, 
            city, 
            search, 
            pageable
        );

        return restaurantPage.map(this::createRestaurantDto);
    }
    
    
    public boolean isUserAssignedToRestaurant(Integer userId, Integer restaurantId) {
        return restaurantUserRepository.existsByRestaurantIdAndUserId(restaurantId, userId);
    }

    // Update a Restaurant
    public RestaurantDTO updateRestaurant(Integer userId, Integer restaurantId, Restaurant updatedRestaurant) {
        if (permissionUtils.isAdminOrAssignedManager(userId, restaurantId)) {
            Restaurant existingRestaurant = restaurantRepository.findById(restaurantId)
                    .orElseThrow(() -> new RestaurantNotFoundException("Restaurant not found with ID: " + restaurantId));

            existingRestaurant.setName(updatedRestaurant.getName());
            existingRestaurant.setAddress(updatedRestaurant.getAddress());
            existingRestaurant.setCity(updatedRestaurant.getCity());
            existingRestaurant.setState(updatedRestaurant.getState());
            existingRestaurant.setPhone(updatedRestaurant.getPhone());
            existingRestaurant.setEmail(updatedRestaurant.getEmail());
            existingRestaurant.setLeadStatus(updatedRestaurant.getLeadStatus());
            existingRestaurant.setAnnualRevenue(updatedRestaurant.getAnnualRevenue());
            existingRestaurant.setTimezone(updatedRestaurant.getTimezone());
            return dtoConverter.convertToRestaurantDTO(existingRestaurant);
        } else {
            throw new UnauthorizedAccessException("You are not authorized to update this restaurant.");
        }
    }


    // Delete a Restaurant
    public void deleteRestaurant(Integer userId, Integer id) {
        if (permissionUtils.isAdminOrAssignedManager(userId, id)) {
        	Optional<Restaurant> restaurant = restaurantRepository.findById(id);
            restaurant.orElseThrow(() -> new RestaurantNotFoundException("Restaurant not found with ID: " + id));
            restaurantRepository.deleteById(id);
        } else {
            throw new UnauthorizedAccessException("You are not authorized to delete this restaurant.");
        }
    }
    
    private RestaurantDTO createRestaurantDto(Restaurant restaurant) {
        if (restaurant == null) {
            throw new IllegalArgumentException("Restaurant cannot be null");
        }
        
        return dtoConverter.convertToRestaurantDTO(restaurant);

    }

}
