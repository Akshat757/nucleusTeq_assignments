package com.example.restaurantmanagement.controller;

import com.example.restaurantmanagement.dto.TopRestaurantDTO;
import com.example.restaurantmanagement.model.Orders;
import com.example.restaurantmanagement.service.OrderService;
import com.example.restaurantmanagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    @PostMapping("/place")
    public ResponseEntity<?> placeOrder(@RequestParam Long customerId) {
        try {
            Orders order = orderService.placeOrder(customerId);
            return new ResponseEntity<>(order, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/customer/{customerId}")
    public List<Orders> getOrdersByCustomer(@PathVariable Long customerId) {
        return orderService.getOrdersByCustomer(customerId);
    }

    @GetMapping("/{orderId}")
    public Orders getOrderById(@PathVariable Long orderId) {
        return orderService.getOrderById(orderId);
    }

    // ✅ Get Orders by Owner
    @GetMapping("/owner/{ownerId}")
    public ResponseEntity<?> getOrdersByOwner(@PathVariable Long ownerId) {
        try {
            List<Orders> orders = orderService.getOrdersByOwner(ownerId);
            return new ResponseEntity<>(orders, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
