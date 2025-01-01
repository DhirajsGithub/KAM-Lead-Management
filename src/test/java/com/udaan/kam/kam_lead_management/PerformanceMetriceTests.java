package com.udaan.kam.kam_lead_management;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.udaan.kam.kam_lead_management.entity.PerformanceMetric;
import com.udaan.kam.kam_lead_management.entity.Restaurant;
import com.udaan.kam.kam_lead_management.exception.BadRequestException;
import com.udaan.kam.kam_lead_management.exception.PerformanceMetricNotFoundException;
import com.udaan.kam.kam_lead_management.repository.PerformanceMetricRepository;
import com.udaan.kam.kam_lead_management.service.PerformanceMetricService;
import com.udaan.kam.kam_lead_management.service.RestaurantService;
import com.udaan.kam.kam_lead_management.util.PermissionUtils;

@ExtendWith(MockitoExtension.class)
public class PerformanceMetriceTests {

    @Mock
    private PerformanceMetricRepository performanceMetricRepository;

    @Mock
    private RestaurantService restaurantService;

    @Mock
    private PermissionUtils permissionUtils;

    @InjectMocks
    private PerformanceMetricService performanceMetricService;

    @Test
    void testCreateMetric_Success() {
        Integer restaurantId = 1;
        Integer userId = 101;
        PerformanceMetric metric = new PerformanceMetric();
        metric.setMetricDate(LocalDateTime.now());
        metric.setLeadsCount(10);
        metric.setClosedDeals(5);
        metric.setRevenue(new BigDecimal("2000.00"));
        metric.setFollowUpsCount(8);

        Restaurant restaurant = new Restaurant();
        restaurant.setId(restaurantId);

        Mockito.when(permissionUtils.isAdminOrAssignedManager(userId, restaurantId)).thenReturn(true);
        Mockito.when(restaurantService.getRestaurantById(restaurantId)).thenReturn(restaurant);
        Mockito.when(performanceMetricRepository.save(metric)).thenReturn(metric);

        PerformanceMetric createdMetric = performanceMetricService.createMetric(userId, metric, restaurantId);

        assertEquals(metric, createdMetric);
        Mockito.verify(performanceMetricRepository).save(metric);
    }

    @Test
    void testCreateMetric_Unauthorized() {
        Integer restaurantId = 1;
        Integer userId = 101;
        PerformanceMetric metric = new PerformanceMetric();

        Mockito.when(permissionUtils.isAdminOrAssignedManager(userId, restaurantId)).thenReturn(false);

        assertThrows(BadRequestException.class, () -> performanceMetricService.createMetric(userId, metric, restaurantId));

        Mockito.verify(performanceMetricRepository, Mockito.never()).save(metric);
    }

    @Test
    void testGetMetricsByRestaurantId_Success() {
        Integer restaurantId = 1;
        Integer userId = 101;
        List<PerformanceMetric> metrics = List.of(new PerformanceMetric(), new PerformanceMetric());

        Mockito.when(permissionUtils.isAdminOrAssignedManager(userId, restaurantId)).thenReturn(true);
        Mockito.when(performanceMetricRepository.findByRestaurantId(restaurantId)).thenReturn(metrics);

        List<PerformanceMetric> result = performanceMetricService.getMetricsByRestaurantId(userId, restaurantId);

        assertEquals(metrics, result);
        Mockito.verify(performanceMetricRepository).findByRestaurantId(restaurantId);
    }

    @Test
    void testGetMetricsByRestaurantId_Unauthorized() {
        Integer restaurantId = 1;
        Integer userId = 101;

        Mockito.when(permissionUtils.isAdminOrAssignedManager(userId, restaurantId)).thenReturn(false);

        assertThrows(BadRequestException.class, () -> performanceMetricService.getMetricsByRestaurantId(userId, restaurantId));

        Mockito.verify(performanceMetricRepository, Mockito.never()).findByRestaurantId(restaurantId);
    }

    @Test
    void testUpdateMetric_Success() {
        Integer restaurantId = 1;
        Integer userId = 101;
        Integer metricId = 1;

        PerformanceMetric existingMetric = new PerformanceMetric();
        existingMetric.setId(metricId);
        existingMetric.setLeadsCount(10);
        existingMetric.setClosedDeals(5);
        existingMetric.setRevenue(new BigDecimal("2000.00"));
        existingMetric.setFollowUpsCount(8);

        PerformanceMetric updatedMetric = new PerformanceMetric();
        updatedMetric.setLeadsCount(15);
        updatedMetric.setClosedDeals(7);
        updatedMetric.setRevenue(new BigDecimal("2500.00"));
        updatedMetric.setFollowUpsCount(10);
        updatedMetric.setMetricDate(LocalDateTime.now().plusDays(1));

        Mockito.when(performanceMetricRepository.findById(metricId)).thenReturn(Optional.of(existingMetric));
        Mockito.when(permissionUtils.isAdminOrAssignedManager(userId, restaurantId)).thenReturn(true);
        Mockito.when(performanceMetricRepository.save(existingMetric)).thenReturn(existingMetric);

        PerformanceMetric result = performanceMetricService.updateMetric(userId, updatedMetric, restaurantId, metricId);

        assertEquals(existingMetric, result);
        Mockito.verify(performanceMetricRepository).save(existingMetric);
    }

    @Test
    void testUpdateMetric_Unauthorized() {
        Integer restaurantId = 1;
        Integer userId = 101;
        Integer metricId = 1;
        PerformanceMetric updatedMetric = new PerformanceMetric();

        Mockito.when(performanceMetricRepository.findById(metricId)).thenReturn(Optional.of(new PerformanceMetric()));
        Mockito.when(permissionUtils.isAdminOrAssignedManager(userId, restaurantId)).thenReturn(false);

        assertThrows(BadRequestException.class, () -> performanceMetricService.updateMetric(userId, updatedMetric, restaurantId, metricId));

        Mockito.verify(performanceMetricRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    void testDeleteMetric_Success() {
        Integer restaurantId = 1;
        Integer userId = 101;
        Integer metricId = 1;

        PerformanceMetric metric = new PerformanceMetric();
        metric.setId(metricId);

        Mockito.when(performanceMetricRepository.findById(metricId)).thenReturn(Optional.of(metric));
        Mockito.when(permissionUtils.isAdminOrAssignedManager(userId, restaurantId)).thenReturn(true);

        performanceMetricService.deleteMetric(userId, metricId, restaurantId);

        Mockito.verify(performanceMetricRepository).delete(metric);
    }

    @Test
    void testDeleteMetric_Unauthorized() {
        Integer restaurantId = 1;
        Integer userId = 101;
        Integer metricId = 1;

        Mockito.when(permissionUtils.isAdminOrAssignedManager(userId, restaurantId)).thenReturn(false);

        assertThrows(BadRequestException.class, () -> performanceMetricService.deleteMetric(userId, metricId, restaurantId));

        Mockito.verify(performanceMetricRepository, Mockito.never()).delete(Mockito.any());
    }

    @Test
    void testDeleteMetric_NotFound() {
        Integer restaurantId = 1;
        Integer userId = 101;
        Integer metricId = 1;

        Mockito.when(permissionUtils.isAdminOrAssignedManager(userId, restaurantId)).thenReturn(true);
        
        Mockito.when(performanceMetricRepository.findById(metricId)).thenReturn(Optional.empty());
        assertThrows(PerformanceMetricNotFoundException.class, () -> performanceMetricService.deleteMetric(userId, metricId, restaurantId));

        Mockito.verify(performanceMetricRepository, Mockito.never()).delete(Mockito.any());
    }



}
