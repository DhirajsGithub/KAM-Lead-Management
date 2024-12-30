package com.udaan.kam.kam_lead_management.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.udaan.kam.kam_lead_management.entity.CallSchedule;

public interface CallScheduleRepository extends JpaRepository<CallSchedule, Integer> {
    List<CallSchedule> findByRestaurantId(Integer restaurantId);
}
