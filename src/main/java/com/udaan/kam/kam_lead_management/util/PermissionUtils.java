package com.udaan.kam.kam_lead_management.util;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.udaan.kam.kam_lead_management.entity.User;
import com.udaan.kam.kam_lead_management.repository.UserRepository;
import com.udaan.kam.kam_lead_management.service.RestaurantUserService;

@Component
public class PermissionUtils {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RestaurantUserService restaurantUserService;

    public boolean isAdminOrAssignedManager(Integer userId, Integer restaurantId) {
        return isAdmin(userId) || restaurantUserService.isRestaurantAssignedToUser(restaurantId, userId);
    }

    public boolean isAdmin(Integer userId) {
        Optional<User> user = userRepository.findById(userId);
        return user.isPresent() && user.get().getRole().toString().equals("ADMIN");
    }
}
