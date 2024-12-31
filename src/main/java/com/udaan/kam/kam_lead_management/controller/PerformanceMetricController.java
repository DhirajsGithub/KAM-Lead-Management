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

    @GetMapping("/{userId}")
    public List<PerformanceMetric> getMetricsByUserId(
            @PathVariable Integer userId,
            @AuthenticationPrincipal UserDetailsImpl currentUser) {
        Integer requestingUserId = currentUser.getUserId();
        return performanceMetricService.getMetricsByUserId(userId, requestingUserId);
    }

    @PostMapping("/{userId}")
    public PerformanceMetric createMetric(
            @PathVariable Integer userId,
            @RequestBody PerformanceMetric metric,
            @AuthenticationPrincipal UserDetailsImpl currentUser) {
        Integer requestingUserId = currentUser.getUserId();
        return performanceMetricService.createMetric(userId, metric, requestingUserId);
    }

    @PutMapping("/{metricId}")
    public PerformanceMetric updateMetric(
            @PathVariable Integer metricId,
            @RequestBody PerformanceMetric updatedMetric,
            @AuthenticationPrincipal UserDetailsImpl currentUser) {
        Integer requestingUserId = currentUser.getUserId();
        return performanceMetricService.updateMetric(metricId, updatedMetric, requestingUserId);
    }

    @DeleteMapping("/{metricId}")
    public void deleteMetric(
            @PathVariable Integer metricId,
            @AuthenticationPrincipal UserDetailsImpl currentUser) {
        Integer requestingUserId = currentUser.getUserId();
        performanceMetricService.deleteMetric(metricId, requestingUserId);
    }
}
