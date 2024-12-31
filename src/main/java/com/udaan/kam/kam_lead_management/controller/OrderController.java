package com.udaan.kam.kam_lead_management.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.udaan.kam.kam_lead_management.entity.Order;
import com.udaan.kam.kam_lead_management.security.UserDetailsImpl;
import com.udaan.kam.kam_lead_management.service.OrderService;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/{restaurantId}")
    public List<Order> getOrdersByRestaurantId(
            @PathVariable Integer restaurantId,
            @AuthenticationPrincipal UserDetailsImpl currentUser) {
        Integer userId = currentUser.getUserId();
        return orderService.getOrdersByRestaurantId(restaurantId, userId);
    }

    @PostMapping("/{restaurantId}")
    public Order createOrder(
            @PathVariable Integer restaurantId,
            @RequestBody Order order,
            @AuthenticationPrincipal UserDetailsImpl currentUser) {
        Integer userId = currentUser.getUserId();
        return orderService.createOrder(restaurantId, order, userId);
    }

    @PutMapping("/{restaurantId}/{orderId}")
    public Order updateOrder(
            @PathVariable Integer restaurantId,
            @PathVariable Integer orderId,
            @RequestBody Order updatedOrder,
            @AuthenticationPrincipal UserDetailsImpl currentUser) {
        Integer userId = currentUser.getUserId();
        return orderService.updateOrder(restaurantId, orderId, updatedOrder, userId);
    }

    @DeleteMapping("/{restaurantId}/{orderId}")
    public void deleteOrder(
            @PathVariable Integer restaurantId,
            @PathVariable Integer orderId,
            @AuthenticationPrincipal UserDetailsImpl currentUser) {
        Integer userId = currentUser.getUserId();
        orderService.deleteOrder(restaurantId, orderId, userId);
    }
}
