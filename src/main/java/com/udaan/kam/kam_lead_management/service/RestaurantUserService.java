package com.udaan.kam.kam_lead_management.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.udaan.kam.kam_lead_management.entity.RestaurantUser;
import com.udaan.kam.kam_lead_management.exception.DuplicateRelationshipException;
import com.udaan.kam.kam_lead_management.exception.ResourceNotFoundException;
import com.udaan.kam.kam_lead_management.repository.RestaurantUserRepository;

@Service
public class RestaurantUserService {

    private final RestaurantUserRepository restaurantUserRepository;

    @Autowired
    public RestaurantUserService(RestaurantUserRepository restaurantUserRepository) {
        this.restaurantUserRepository = restaurantUserRepository;
    }

    // Add a user to a restaurant with duplicate check
    @Transactional
    public RestaurantUser addUserToRestaurant(RestaurantUser restaurantUser) {
        Optional<RestaurantUser> existingRelationship = restaurantUserRepository
            .findByRestaurantIdAndUserId(
                restaurantUser.getRestaurant().getId(), 
                restaurantUser.getUser().getId()
            );
        
        if (existingRelationship.isPresent()) {
            if (!existingRelationship.get().getIsActive()) {
                RestaurantUser existing = existingRelationship.get();
                existing.setIsActive(true);
                return restaurantUserRepository.save(existing);
            }
            throw new DuplicateRelationshipException(
                String.format("User ID %d is already assigned to Restaurant ID %d", 
                    restaurantUser.getUser().getId(), 
                    restaurantUser.getRestaurant().getId())
            );
        }
        
        return restaurantUserRepository.save(restaurantUser);
    }
    
    // Get all users for a specific restaurant
    public List<RestaurantUser> getUsersByRestaurant(Integer restaurantId) {
        return restaurantUserRepository.findByRestaurantId(restaurantId);
    }

    // Get all restaurants for a specific user
    public List<RestaurantUser> getRestaurantsByUser(Integer userId) {
        return restaurantUserRepository.findByUserId(userId);
    }

    // Delete a user-restaurant relationship
    @Transactional
    public void deleteUserRestaurant(Integer restaurantId, Integer userId) {
        Optional<RestaurantUser> restaurantUser = restaurantUserRepository.findByRestaurantIdAndUserId(restaurantId, userId);
        if(!restaurantUser.isPresent()) {
        	String message = String.format("Can't find resturant with id %d and user with id %d", restaurantId, userId);
        	throw new ResourceNotFoundException(message);
        }
        restaurantUser.ifPresent(restaurantUserRepository::delete);
    }



    public boolean isRestaurantAssignedToUser(Integer restaurantId, Integer userId) {
        return restaurantUserRepository.existsByRestaurantIdAndUserId(restaurantId, userId);
    }
}
