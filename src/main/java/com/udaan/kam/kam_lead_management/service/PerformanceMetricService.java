package com.udaan.kam.kam_lead_management.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.udaan.kam.kam_lead_management.entity.PerformanceMetric;
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
    private UserRepository userRepository;
    
    @Autowired
    private PermissionUtils permissionUtils;

    public List<PerformanceMetric> getMetricsByUserId(Integer userId, Integer requestingUserId) {
        if (!permissionUtils.isAdmin(requestingUserId) && !requestingUserId.equals(userId)) {
            throw new BadRequestException("You are not authorized to view metrics for this user.");
        }
        return performanceMetricRepository.findByUserId(userId);
    }

    public PerformanceMetric createMetric(Integer userId, PerformanceMetric metric, Integer requestingUserId) {
        if (!permissionUtils.isAdmin(requestingUserId)) {
            throw new BadRequestException("You are not authorized to create metrics for this user.");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BadRequestException("User not found with ID: " + userId));

        metric.setUser(user);
        return performanceMetricRepository.save(metric);
    }

    public PerformanceMetric updateMetric(Integer metricId, PerformanceMetric updatedMetric, Integer requestingUserId) {
        PerformanceMetric existingMetric = performanceMetricRepository.findById(metricId)
                .orElseThrow(() -> new PerformanceMetricNotFoundException("Metric not found with ID: " + metricId));

        if (!permissionUtils.isAdmin(requestingUserId)) {
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

    public void deleteMetric(Integer metricId, Integer requestingUserId) {
        PerformanceMetric metric = performanceMetricRepository.findById(metricId)
                .orElseThrow(() -> new PerformanceMetricNotFoundException("Metric not found with ID: " + metricId));

        if (!permissionUtils.isAdmin(requestingUserId)) {
            throw new BadRequestException("You are not authorized to delete this metric.");
        }

        performanceMetricRepository.delete(metric);
    }
    
}
