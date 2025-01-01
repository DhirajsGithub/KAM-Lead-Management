package com.udaan.kam.kam_lead_management.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.udaan.kam.kam_lead_management.entity.PerformanceMetric;
import com.udaan.kam.kam_lead_management.entity.Restaurant;
import com.udaan.kam.kam_lead_management.entity.User;
import com.udaan.kam.kam_lead_management.exception.BadRequestException;
import com.udaan.kam.kam_lead_management.exception.PerformanceMetricNotFoundException;
import com.udaan.kam.kam_lead_management.repository.PerformanceMetricRepository;
import com.udaan.kam.kam_lead_management.repository.UserRepository;
import com.udaan.kam.kam_lead_management.util.PermissionUtils;

@Service
public class PerformanceMetricService {

    @Autowired
    private PerformanceMetricRepository performanceMetricRepository;

    
    @Autowired
    private PermissionUtils permissionUtils;
    
    @Autowired
	private RestaurantService restaurantService;
    
    

    public List<PerformanceMetric> getMetricsByRestaurantId(Integer userId, Integer restaurantId) {
        if (!permissionUtils.isAdminOrAssignedManager(userId, restaurantId)) {
            throw new BadRequestException("You are not authorized to view metrics for this restauarnt.");
        }
        return performanceMetricRepository.findByRestaurantId(restaurantId);
    }
    
    public PerformanceMetric createMetric(Integer userId, PerformanceMetric metric, Integer restaurantId) {
    	if (!permissionUtils.isAdminOrAssignedManager(userId, restaurantId)) {
            throw new BadRequestException("You are not authorized to create metrics for this restauarnt.");
        }

        Restaurant restaurant = restaurantService.getRestaurantById(restaurantId);
        metric.setRestaurant(restaurant);
        return performanceMetricRepository.save(metric);
    }

    public PerformanceMetric updateMetric(Integer userId, PerformanceMetric updatedMetric, Integer restaurantId, Integer metricId) {
        PerformanceMetric existingMetric = performanceMetricRepository.findById(metricId)
                .orElseThrow(() -> new PerformanceMetricNotFoundException("Metric not found with ID: " + metricId));

    	if (!permissionUtils.isAdminOrAssignedManager(userId, restaurantId)) {
            throw new BadRequestException("You are not authorized to update this metric.");
        }

        existingMetric.setLeadsCount(updatedMetric.getLeadsCount());
        existingMetric.setClosedDeals(updatedMetric.getClosedDeals());
        existingMetric.setRevenue(updatedMetric.getRevenue());
        existingMetric.setFollowUpsCount(updatedMetric.getFollowUpsCount());
        if (updatedMetric.getMetricDate() != null) {
        	existingMetric.setMetricDate(updatedMetric.getMetricDate().toString());
        }
        return performanceMetricRepository.save(existingMetric);
    }

    public void deleteMetric(Integer userId, Integer metricId, Integer restaurantId) {
    	
    	if (!permissionUtils.isAdminOrAssignedManager(userId, restaurantId)) {
            throw new BadRequestException("You are not authorized to update this metric.");
        }
    	
        PerformanceMetric metric = performanceMetricRepository.findById(metricId)
                .orElseThrow(() -> new PerformanceMetricNotFoundException("Metric not found with ID: " + metricId));


        performanceMetricRepository.delete(metric);
    }
    
}
