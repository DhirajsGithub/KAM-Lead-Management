package com.udaan.kam.kam_lead_management;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.udaan.kam.kam_lead_management.DTO.RestaurantDTO;
import com.udaan.kam.kam_lead_management.entity.Restaurant;
import com.udaan.kam.kam_lead_management.entity.Restaurant.LeadStatus;
import com.udaan.kam.kam_lead_management.exception.RestaurantNotFoundException;
import com.udaan.kam.kam_lead_management.repository.RestaurantRepository;
import com.udaan.kam.kam_lead_management.service.RestaurantService;
import com.udaan.kam.kam_lead_management.util.DTOConverterUtil;
import com.udaan.kam.kam_lead_management.util.PermissionUtils;

@ExtendWith(MockitoExtension.class)
public class RestaurantTests {

    @Mock
    private RestaurantRepository restaurantRepository;

    @Mock
    private DTOConverterUtil dtoConverter;

    @InjectMocks
    private RestaurantService restaurantService;

    private Restaurant sampleRestaurant;
    private RestaurantDTO sampleRestaurantDTO;
    
    @Mock
    private PermissionUtils permissionUtils;

    @BeforeEach
    void setUp() {
        sampleRestaurant = new Restaurant(
                "Test Restaurant", "123 Main St", "Test City", "Test State",
                "+1234567890", "test@example.com", LeadStatus.NEW, BigDecimal.valueOf(50000)
        );
        sampleRestaurant.setId(1);

        sampleRestaurantDTO = new RestaurantDTO(
                1, "Test Restaurant", "123 Main St", "Test City", "Test State",
                "+1234567890", "test@example.com", null, "NEW",
                BigDecimal.valueOf(50000), "GMT+5:30"
        );
    }

    @Test
    void testCreateRestaurant_Success() {
        when(restaurantRepository.save(any(Restaurant.class))).thenReturn(sampleRestaurant);
        when(dtoConverter.convertToRestaurantDTO(any(Restaurant.class))).thenReturn(sampleRestaurantDTO);

        RestaurantDTO result = restaurantService.createRestaurant(sampleRestaurant);

        assertNotNull(result);
        assertEquals(sampleRestaurant.getName(), result.getName());
        verify(restaurantRepository, times(1)).save(sampleRestaurant);
    }

    @Test
    void testGetRestaurantById_Success() {
        when(restaurantRepository.findById(1)).thenReturn(Optional.of(sampleRestaurant));

        Restaurant result = restaurantService.getRestaurantById(1);

        assertNotNull(result);
        assertEquals(sampleRestaurant.getName(), result.getName());
        verify(restaurantRepository, times(1)).findById(1);
    }

    @Test
    void testGetRestaurantById_NotFound() {
        when(restaurantRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(RestaurantNotFoundException.class, () -> {
            restaurantService.getRestaurantById(1);
        });
    }

    @Test
    void testUpdateRestaurant_Success() {
        Restaurant updatedRestaurant = new Restaurant(
                "Updated Name", "456 Updated St", "Updated City", "Updated State",
                "+9876543210", "updated@example.com", LeadStatus.CONTACTED, BigDecimal.valueOf(100000)
        );

        RestaurantDTO updatedRestaurantDTO = new RestaurantDTO(
                1, "Updated Name", "456 Updated St", "Updated City", "Updated State",
                "+9876543210", "updated@example.com", null, "CONTACTED",
                BigDecimal.valueOf(100000), "GMT+5:30"
        );

        when(permissionUtils.isAdminOrAssignedManager(anyInt(), anyInt())).thenReturn(true);

        when(restaurantRepository.findById(1)).thenReturn(Optional.of(sampleRestaurant));
        when(dtoConverter.convertToRestaurantDTO(any(Restaurant.class))).thenReturn(updatedRestaurantDTO);

        RestaurantDTO result = restaurantService.updateRestaurant(1, 1, updatedRestaurant);

        assertNotNull(result);
        assertEquals(updatedRestaurant.getName(), result.getName()); 
        assertEquals(updatedRestaurant.getAddress(), result.getAddress()); 

        verify(permissionUtils, times(1)).isAdminOrAssignedManager(1, 1); 
        verify(restaurantRepository, times(1)).findById(1);
    }



    @Test
    void testDeleteRestaurant_Success() {
        when(permissionUtils.isAdminOrAssignedManager(anyInt(), anyInt())).thenReturn(true);

        when(restaurantRepository.findById(1)).thenReturn(Optional.of(sampleRestaurant));

        restaurantService.deleteRestaurant(1, 1);

        verify(restaurantRepository, times(1)).deleteById(1);
    }


    @Test
    void testDeleteRestaurant_NotFound() {
        // Mock permission check to return true
        when(permissionUtils.isAdminOrAssignedManager(anyInt(), anyInt())).thenReturn(true);

        // Mock restaurantRepository to return Optional.empty()
        when(restaurantRepository.findById(1)).thenReturn(Optional.empty());

        // Assert that RestaurantNotFoundException is thrown
        assertThrows(RestaurantNotFoundException.class, () -> {
            restaurantService.deleteRestaurant(1, 1);
        });

        // Verify that no delete operation was attempted
        verify(restaurantRepository, times(0)).deleteById(anyInt());
    }


    @Test
    void testGetFilteredRestaurants_Success() {
        List<Restaurant> restaurants = List.of(sampleRestaurant);
        Page<Restaurant> restaurantPage = new PageImpl<>(restaurants);
        Pageable pageable = PageRequest.of(0, 10);

        when(restaurantRepository.findByFiltersAndSearch(null, "Test City", "Test", pageable))
                .thenReturn(restaurantPage);

        Page<RestaurantDTO> result = restaurantService.getFilteredRestaurants(null, "Test City", "Test", 0, 10);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(restaurantRepository, times(1)).findByFiltersAndSearch(null, "Test City", "Test", pageable);
    }

    // Integration Test
    @DataJpaTest
    static class RestaurantServiceIntegrationTest {

        @Autowired
        private RestaurantRepository restaurantRepository;

        @Test
        void testCreateAndRetrieveRestaurant() {
            Restaurant restaurant = new Restaurant(
                    "Integration Test", "123 Integration St", "Integration City", "Integration State",
                    "+1112223334", "integration@example.com", LeadStatus.NEW, BigDecimal.valueOf(80000)
            );

            Restaurant savedRestaurant = restaurantRepository.save(restaurant);

            Optional<Restaurant> foundRestaurant = restaurantRepository.findById(savedRestaurant.getId());

            assertTrue(foundRestaurant.isPresent());
            assertEquals("Integration Test", foundRestaurant.get().getName());
        }
    }
}
