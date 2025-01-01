package com.udaan.kam.kam_lead_management;



import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.udaan.kam.kam_lead_management.entity.Restaurant;
import com.udaan.kam.kam_lead_management.entity.RestaurantUser;
import com.udaan.kam.kam_lead_management.entity.User;
import com.udaan.kam.kam_lead_management.exception.DuplicateRelationshipException;
import com.udaan.kam.kam_lead_management.exception.ResourceNotFoundException;
import com.udaan.kam.kam_lead_management.repository.RestaurantUserRepository;
import com.udaan.kam.kam_lead_management.service.RestaurantUserService;

@ExtendWith(MockitoExtension.class)
public class RestaurantUserTests {

    @Mock
    private RestaurantUserRepository restaurantUserRepository;  // Mocked Repository

    @InjectMocks
    private RestaurantUserService restaurantUserService;  // Service class with injected mocks

    // Test for adding a user to a restaurant successfully
    @Test
    void testAddUserToRestaurant_Success() {
        Restaurant restaurant = new Restaurant();
        restaurant.setId(1);
        User user = new User();
        user.setId(1);
        RestaurantUser restaurantUser = new RestaurantUser(restaurant, user, true);

        // Mock repository behavior
        when(restaurantUserRepository.findByRestaurantIdAndUserId(1, 1)).thenReturn(Optional.empty());
        when(restaurantUserRepository.save(restaurantUser)).thenReturn(restaurantUser);

        RestaurantUser result = restaurantUserService.addUserToRestaurant(restaurantUser);

        assertNotNull(result);
        assertTrue(result.getIsActive());
        verify(restaurantUserRepository).save(restaurantUser);
    }

    // Test for adding a user to a restaurant when the relationship exists and is inactive
    @Test
    void testAddUserToRestaurant_RestoreInactive() {
        Restaurant restaurant = new Restaurant();
        restaurant.setId(1);
        User user = new User();
        user.setId(1);
        RestaurantUser restaurantUser = new RestaurantUser(restaurant, user, true);

        // Mock existing inactive relationship
        RestaurantUser existingRestaurantUser = new RestaurantUser(restaurant, user, false);

        // Mock repository behavior
        when(restaurantUserRepository.findByRestaurantIdAndUserId(1, 1)).thenReturn(Optional.of(existingRestaurantUser));
        when(restaurantUserRepository.save(existingRestaurantUser)).thenReturn(existingRestaurantUser);

        RestaurantUser result = restaurantUserService.addUserToRestaurant(restaurantUser);

        assertNotNull(result);
        assertTrue(result.getIsActive());
        verify(restaurantUserRepository).save(existingRestaurantUser);
    }

    // Test for adding a user to a restaurant when the relationship exists and is already active
    @Test
    void testAddUserToRestaurant_Duplicate() {
        Restaurant restaurant = new Restaurant();
        restaurant.setId(1);
        User user = new User();
        user.setId(1);
        RestaurantUser restaurantUser = new RestaurantUser(restaurant, user, true);

        // Mock existing active relationship
        RestaurantUser existingRestaurantUser = new RestaurantUser(restaurant, user, true);

        // Mock repository behavior
        when(restaurantUserRepository.findByRestaurantIdAndUserId(1, 1)).thenReturn(Optional.of(existingRestaurantUser));

        // Assert that DuplicateRelationshipException is thrown
        assertThrows(DuplicateRelationshipException.class, () -> {
            restaurantUserService.addUserToRestaurant(restaurantUser);
        });

        // Ensure that save is never called for a duplicate
        verify(restaurantUserRepository, never()).save(any());
    }

    // Test for deleting a user-restaurant relationship successfully
    @Test
    void testDeleteUserRestaurant_Success() {
        Integer restaurantId = 1;
        Integer userId = 1;
        RestaurantUser restaurantUser = new RestaurantUser();
        restaurantUser.setId(1);

        // Mock repository behavior
        when(restaurantUserRepository.findByRestaurantIdAndUserId(restaurantId, userId)).thenReturn(Optional.of(restaurantUser));

        // Perform deletion
        restaurantUserService.deleteUserRestaurant(restaurantId, userId);

        // Verify that delete was called
        verify(restaurantUserRepository).delete(restaurantUser);
    }

    // Test for deleting a user-restaurant relationship when not found
    @Test
    void testDeleteUserRestaurant_NotFound() {
        Integer restaurantId = 1;
        Integer userId = 1;

        // Mock repository behavior for not found
        when(restaurantUserRepository.findByRestaurantIdAndUserId(restaurantId, userId)).thenReturn(Optional.empty());

        // Assert that ResourceNotFoundException is thrown
        assertThrows(ResourceNotFoundException.class, () -> {
            restaurantUserService.deleteUserRestaurant(restaurantId, userId);
        });

        // Ensure delete was never called
        verify(restaurantUserRepository, never()).delete(any());
    }
}
