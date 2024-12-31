package com.udaan.kam.kam_lead_management.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.udaan.kam.kam_lead_management.entity.PerformanceMetric;

public interface PerformanceMetricRepository extends JpaRepository <PerformanceMetric, Integer> {
	List<PerformanceMetric> findByUserId(Integer userId);
}
