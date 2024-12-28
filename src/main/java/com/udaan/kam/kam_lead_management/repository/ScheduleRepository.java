package com.udaan.kam.kam_lead_management.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.udaan.kam.kam_lead_management.entity.CallSchedule;

public interface ScheduleRepository extends JpaRepository<CallSchedule, Integer> {

}
