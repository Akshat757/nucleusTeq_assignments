package com.example.restaurantmanagement.service;

import com.example.restaurantmanagement.dto.TopRestaurantDTO;
import com.example.restaurantmanagement.model.Orders;
import java.util.List;

public interface OrderService {
    Orders placeOrder(Long customerId);
    List<Orders> getOrdersByCustomer(Long customerId);
    Orders getOrderById(Long orderId);
    List<Orders> getOrdersByOwner(Long ownerId);
    List<TopRestaurantDTO> getTopRestaurantsByOwner(Long ownerId);
}
