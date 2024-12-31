package com.udaan.kam.kam_lead_management.service;

import com.udaan.kam.kam_lead_management.entity.Order;
import com.udaan.kam.kam_lead_management.entity.Restaurant;
import com.udaan.kam.kam_lead_management.entity.User;
import com.udaan.kam.kam_lead_management.exception.BadRequestException;
import com.udaan.kam.kam_lead_management.exception.OrderNotFoundException;
import com.udaan.kam.kam_lead_management.repository.OrderRepository;
import com.udaan.kam.kam_lead_management.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RestaurantUserService restaurantUserService;

    public List<Order> getOrdersByRestaurantId(Integer restaurantId, Integer userId) {
        if (!isAdminOrAssignedManager(userId, restaurantId)) {
            throw new BadRequestException("You are not authorized to view orders for this restaurant.");
        }
        return orderRepository.findByRestaurantId(restaurantId);
    }

    public Order createOrder(Integer restaurantId, Order order, Integer userId) {
        if (!isAdminOrAssignedManager(userId, restaurantId)) {
            throw new BadRequestException("You are not authorized to create orders for this restaurant.");
        }

        // getRestaurantById throws error if resturant is not found
        Restaurant restaurant = restaurantService.getRestaurantById(restaurantId);
        
        order.setRestaurant(restaurant);
        return orderRepository.save(order);
    }

    public Order updateOrder(Integer restaurantId, Integer orderId, Order updatedOrder, Integer userId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with ID: " + orderId));

        if (!isAdminOrAssignedManager(userId, restaurantId)) {
            throw new BadRequestException("You are not authorized to update this order.");
        }
        
        if (order.getOrderDate() != null) {
            order.setOrderDate(updatedOrder.getOrderDate().toString());
        }

        order.setTotalAmount(updatedOrder.getTotalAmount());
        order.setStatus(updatedOrder.getStatus());

        return orderRepository.save(order);
    }

    public void deleteOrder(Integer restaurantId, Integer orderId, Integer userId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with ID: " + orderId));

        if (!isAdminOrAssignedManager(userId, restaurantId)) {
            throw new BadRequestException("You are not authorized to delete this order.");
        }

        orderRepository.delete(order);
    }

    private boolean isAdminOrAssignedManager(Integer userId, Integer restaurantId) {
        return isAdmin(userId) || restaurantUserService.isRestaurantAssignedToUser(restaurantId, userId);
    }

    private boolean isAdmin(Integer userId) {
        Optional<User> user = userRepository.findById(userId);
        return user.isPresent() && user.get().getRole().toString().equals("ADMIN");
    }
}
