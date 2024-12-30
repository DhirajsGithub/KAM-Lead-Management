package com.udaan.kam.kam_lead_management.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.udaan.kam.kam_lead_management.entity.CallSchedule;
import com.udaan.kam.kam_lead_management.service.CallScheduleService;
import com.udaan.kam.kam_lead_management.security.UserDetailsImpl;

@RestController
@RequestMapping("/api/call-schedules")
public class CallScheduleController {

    @Autowired
    private CallScheduleService callScheduleService;

    // Create a new CallSchedule for a specific Restaurant
    @PostMapping("/{restaurantId}")
    public ResponseEntity<CallSchedule> createCallSchedule(
            @PathVariable Integer restaurantId,
            @RequestBody CallSchedule callSchedule,
            @AuthenticationPrincipal UserDetailsImpl currentUser) {
        Integer userId = currentUser.getUserId(); // Get the logged-in user ID
        CallSchedule createdSchedule = callScheduleService.createCallSchedule(callSchedule, restaurantId, userId);
        return new ResponseEntity<>(createdSchedule, HttpStatus.CREATED);
    }

    // Get all CallSchedules for a specific Restaurant
    @GetMapping("/{restaurantId}")
    public List<CallSchedule> getCallSchedulesByRestaurantId(
            @PathVariable Integer restaurantId,
            @AuthenticationPrincipal UserDetailsImpl currentUser) {
        Integer userId = currentUser.getUserId(); // Get the logged-in user ID
        return callScheduleService.getCallSchedulesByRestaurantId(restaurantId, userId);
    }

    // Update CallSchedule by ID for a specific Restaurant
    @PutMapping("/{restaurantId}/{scheduleId}")
    public ResponseEntity<CallSchedule> updateCallSchedule(
            @PathVariable Integer restaurantId,
            @PathVariable Integer scheduleId,
            @RequestBody CallSchedule updatedSchedule,
            @AuthenticationPrincipal UserDetailsImpl currentUser) {
        Integer userId = currentUser.getUserId(); // Get the logged-in user ID
        CallSchedule updated = callScheduleService.updateCallSchedule(scheduleId, updatedSchedule, restaurantId, userId);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    // Delete CallSchedule by ID for a specific Restaurant
    @DeleteMapping("/{restaurantId}/{scheduleId}")
    public ResponseEntity<Void> deleteCallSchedule(
            @PathVariable Integer restaurantId,
            @PathVariable Integer scheduleId,
            @AuthenticationPrincipal UserDetailsImpl currentUser) {
        Integer userId = currentUser.getUserId(); // Get the logged-in user ID
        callScheduleService.deleteCallSchedule(scheduleId, restaurantId, userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
