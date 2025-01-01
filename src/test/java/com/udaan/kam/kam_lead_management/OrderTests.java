package com.udaan.kam.kam_lead_management;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.udaan.kam.kam_lead_management.entity.Order;
import com.udaan.kam.kam_lead_management.entity.Restaurant;
import com.udaan.kam.kam_lead_management.exception.BadRequestException;
import com.udaan.kam.kam_lead_management.exception.OrderNotFoundException;
import com.udaan.kam.kam_lead_management.repository.OrderRepository;
import com.udaan.kam.kam_lead_management.service.OrderService;
import com.udaan.kam.kam_lead_management.service.RestaurantService;
import com.udaan.kam.kam_lead_management.util.PermissionUtils;

@ExtendWith(MockitoExtension.class)
class OrderTests {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private RestaurantService restaurantService;

    @Mock
    private PermissionUtils permissionUtils;

    @InjectMocks
    private OrderService orderService;

    @Test
    void testCreateOrder_Success() {
        Integer restaurantId = 1;
        Integer userId = 101;
        Order order = new Order();
        order.setOrderDate(LocalDateTime.now().toString());
        order.setTotalAmount(new BigDecimal("100.00"));
        order.setStatus(Order.OrderStatus.PENDING);

        Restaurant restaurant = new Restaurant();
        restaurant.setId(restaurantId);

        Mockito.when(permissionUtils.isAdminOrAssignedManager(userId, restaurantId)).thenReturn(true);
        Mockito.when(restaurantService.getRestaurantById(restaurantId)).thenReturn(restaurant);
        Mockito.when(orderRepository.save(order)).thenReturn(order);

        Order createdOrder = orderService.createOrder(restaurantId, order, userId);

        assertEquals(order, createdOrder);
        Mockito.verify(orderRepository).save(order);
    }

    @Test
    void testCreateOrder_Unauthorized() {
        Integer restaurantId = 1;
        Integer userId = 101;
        Order order = new Order();

        Mockito.when(permissionUtils.isAdminOrAssignedManager(userId, restaurantId)).thenReturn(false);

        assertThrows(BadRequestException.class, () -> orderService.createOrder(restaurantId, order, userId));

        Mockito.verify(orderRepository, Mockito.never()).save(order);
    }

    @Test
    void testGetOrdersByRestaurantId_Success() {
        Integer restaurantId = 1;
        Integer userId = 101;
        List<Order> orders = List.of(new Order(), new Order());

        Mockito.when(permissionUtils.isAdminOrAssignedManager(userId, restaurantId)).thenReturn(true);
        Mockito.when(orderRepository.findByRestaurantId(restaurantId)).thenReturn(orders);

        List<Order> result = orderService.getOrdersByRestaurantId(restaurantId, userId);

        assertEquals(orders, result);
        Mockito.verify(orderRepository).findByRestaurantId(restaurantId);
    }

    @Test
    void testGetOrdersByRestaurantId_Unauthorized() {
        Integer restaurantId = 1;
        Integer userId = 101;

        Mockito.when(permissionUtils.isAdminOrAssignedManager(userId, restaurantId)).thenReturn(false);

        assertThrows(BadRequestException.class, () -> orderService.getOrdersByRestaurantId(restaurantId, userId));

        Mockito.verify(orderRepository, Mockito.never()).findByRestaurantId(restaurantId);
    }

    @Test
    void testUpdateOrder_Success() {
        Integer orderId = 1;
        Integer restaurantId = 1;
        Integer userId = 101;

        Order existingOrder = new Order();
        existingOrder.setId(orderId);
        existingOrder.setOrderDate(LocalDateTime.now().toString());
        existingOrder.setTotalAmount(new BigDecimal("100.00"));
        existingOrder.setStatus(Order.OrderStatus.PENDING);

        Order updatedOrder = new Order();
        updatedOrder.setOrderDate(LocalDateTime.now().plusDays(1).toString());
        updatedOrder.setTotalAmount(new BigDecimal("150.00"));
        updatedOrder.setStatus(Order.OrderStatus.CONFIRMED);

        Mockito.when(orderRepository.findById(orderId)).thenReturn(Optional.of(existingOrder));
        Mockito.when(permissionUtils.isAdminOrAssignedManager(userId, restaurantId)).thenReturn(true);
        Mockito.when(orderRepository.save(existingOrder)).thenReturn(existingOrder);

        Order result = orderService.updateOrder(restaurantId, orderId, updatedOrder, userId);

        assertEquals(existingOrder, result);
        Mockito.verify(orderRepository).save(existingOrder);
    }

    @Test
    void testUpdateOrder_Unauthorized() {
        Integer orderId = 1;
        Integer restaurantId = 1;
        Integer userId = 101;
        Order updatedOrder = new Order();

        Mockito.when(orderRepository.findById(orderId)).thenReturn(Optional.of(new Order()));
        Mockito.when(permissionUtils.isAdminOrAssignedManager(userId, restaurantId)).thenReturn(false);

        assertThrows(BadRequestException.class, () -> orderService.updateOrder(restaurantId, orderId, updatedOrder, userId));

        Mockito.verify(orderRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    void testDeleteOrder_Success() {
        Integer orderId = 1;
        Integer restaurantId = 1;
        Integer userId = 101;

        Order order = new Order();
        order.setId(orderId);

        Mockito.when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        Mockito.when(permissionUtils.isAdminOrAssignedManager(userId, restaurantId)).thenReturn(true);

        orderService.deleteOrder(restaurantId, orderId, userId);

        Mockito.verify(orderRepository).delete(order);
    }

    @Test
    void testDeleteOrder_Unauthorized() {
        Integer orderId = 1;
        Integer restaurantId = 1;
        Integer userId = 101;

        Mockito.when(orderRepository.findById(orderId)).thenReturn(Optional.of(new Order()));
        Mockito.when(permissionUtils.isAdminOrAssignedManager(userId, restaurantId)).thenReturn(false);

        assertThrows(BadRequestException.class, () -> orderService.deleteOrder(restaurantId, orderId, userId));

        Mockito.verify(orderRepository, Mockito.never()).delete(Mockito.any());
    }

    @Test
    void testDeleteOrder_NotFound() {
        Integer orderId = 1;
        Integer restaurantId = 1;
        Integer userId = 101;

        Mockito.when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        assertThrows(OrderNotFoundException.class, () -> orderService.deleteOrder(restaurantId, orderId, userId));

        Mockito.verify(orderRepository, Mockito.never()).delete(Mockito.any());
    }
}
