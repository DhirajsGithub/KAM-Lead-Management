package com.udaan.kam.kam_lead_management.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.udaan.kam.kam_lead_management.entity.CallSchedule;
import com.udaan.kam.kam_lead_management.entity.Restaurant;
import com.udaan.kam.kam_lead_management.entity.User;
import com.udaan.kam.kam_lead_management.exception.BadRequestException;
import com.udaan.kam.kam_lead_management.exception.CallScheduleNotFoundException;
import com.udaan.kam.kam_lead_management.repository.CallScheduleRepository;
import com.udaan.kam.kam_lead_management.repository.RestaurantRepository;
import com.udaan.kam.kam_lead_management.repository.UserRepository;

@Service
public class CallScheduleService {

    @Autowired
    private CallScheduleRepository callScheduleRepository;
    
    @Autowired
    private RestaurantRepository restaurantRepository;
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RestaurantUserService restaurantUserService;

    // Create a new CallSchedule for a restaurant
    public CallSchedule createCallSchedule(CallSchedule callSchedule, Integer restaurantId, Integer userId) {
        if (!isAdminOrAssignedManager(userId, restaurantId)) {
            throw new BadRequestException("You are not authorized to add a call schedule for this restaurant.");
        }

        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new BadRequestException("Restaurant not found"));

        callSchedule.setRestaurant(restaurant);
        return callScheduleRepository.save(callSchedule);
    }
    
    // Get all CallSchedules for a specific Restaurant
    public List<CallSchedule> getCallSchedulesByRestaurantId(Integer restaurantId, Integer userId) {
        if (!isAdminOrAssignedManager(userId, restaurantId)) {
            throw new BadRequestException("You are not authorized to view call schedules for this restaurant.");
        }

        return callScheduleRepository.findByRestaurantId(restaurantId);
    }

    // Update CallSchedule by ID
    public CallSchedule updateCallSchedule(Integer scheduleId, CallSchedule updatedSchedule, Integer restaurantId, Integer userId) {
        CallSchedule schedule = callScheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new CallScheduleNotFoundException("Call schedule not found with ID: " + scheduleId));

        if (!isAdminOrAssignedManager(userId, restaurantId)) {
            throw new BadRequestException("You are not authorized to update this call schedule.");
        }
        
        if (updatedSchedule.getLastCallDate() != null) {
            schedule.setLastCallDate(updatedSchedule.getLastCallDate().toString());
        }
        if (updatedSchedule.getNextCallDate() != null) {
            schedule.setNextCallDate(updatedSchedule.getNextCallDate().toString());
        }

        schedule.setFrequencyDays(updatedSchedule.getFrequencyDays());
        schedule.setPriorityLevel(updatedSchedule.getPriorityLevel());
        return callScheduleRepository.save(schedule);
    }

    // Delete CallSchedule by ID
    public void deleteCallSchedule(Integer scheduleId, Integer restaurantId, Integer userId) {
        CallSchedule schedule = callScheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new CallScheduleNotFoundException("Call schedule not found with ID: " + scheduleId));

        if (!isAdminOrAssignedManager(userId, restaurantId)) {
            throw new BadRequestException("You are not authorized to delete this call schedule.");
        }

        callScheduleRepository.deleteById(scheduleId);
    }

    // Check if the user is an Admin or assigned Manager for the restaurant
    private boolean isAdminOrAssignedManager(Integer userId, Integer restaurantId) {
        return isAdmin(userId) || restaurantUserService.isRestaurantAssignedToUser(restaurantId, userId);
    }

    private boolean isAdmin(Integer userId) {
        Optional<User> user = userRepository.findById(userId);
        return user.isPresent() && user.get().getRole().toString().equals("ADMIN");
    }
}
