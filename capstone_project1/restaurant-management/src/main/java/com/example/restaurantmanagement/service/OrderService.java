package com.example.restaurantmanagement.service;

import com.example.restaurantmanagement.model.Order;
import java.util.List;

public interface OrderService {
    Order placeOrder(Long customerId);
    List<Order> getOrdersByCustomer(Long customerId);
    Order getOrderById(Long orderId);
}
