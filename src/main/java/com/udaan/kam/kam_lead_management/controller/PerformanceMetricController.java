package com.udaan.kam.kam_lead_management.controller;

import com.udaan.kam.kam_lead_management.entity.PerformanceMetric;
import com.udaan.kam.kam_lead_management.security.UserDetailsImpl;
import com.udaan.kam.kam_lead_management.service.PerformanceMetricService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/performance_metrics")
public class PerformanceMetricController {

    @Autowired
    private PerformanceMetricService performanceMetricService;

    @GetMapping("/{restaurantId}")
    public List<PerformanceMetric> getMetricsByUserId(
            @PathVariable Integer restaurantId,
            @AuthenticationPrincipal UserDetailsImpl currentUser) {
        Integer userId = currentUser.getUserId();
        return performanceMetricService.getMetricsByRestaurantId(userId, restaurantId);
    }

    @PostMapping("/{restaurantId}")
    public PerformanceMetric createMetric(
            @PathVariable Integer restaurantId,
            @RequestBody PerformanceMetric metric,
            @AuthenticationPrincipal UserDetailsImpl currentUser) {
        Integer userId = currentUser.getUserId();
        return performanceMetricService.createMetric(userId, metric, restaurantId);
    }

    @PutMapping("/{restaurantId}/{metricId}")
    public PerformanceMetric updateMetric(
            @PathVariable Integer metricId,
            @PathVariable Integer restaurantId,
            @RequestBody PerformanceMetric updatedMetric,
            @AuthenticationPrincipal UserDetailsImpl currentUser) {
        Integer userId = currentUser.getUserId();
        return performanceMetricService.updateMetric(userId, updatedMetric, restaurantId,metricId );
    }

    @DeleteMapping("/{restaurantId}/{metricId}")
    public void deleteMetric(
            @PathVariable Integer metricId,
            @PathVariable Integer restaurantId,
            @AuthenticationPrincipal UserDetailsImpl currentUser) {
        Integer userId = currentUser.getUserId();
        performanceMetricService.deleteMetric(userId, metricId, restaurantId);
    }
}
