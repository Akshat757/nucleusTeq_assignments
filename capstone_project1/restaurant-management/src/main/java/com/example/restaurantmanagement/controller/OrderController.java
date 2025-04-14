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
@RequestMapping("/api/orders") // Base path for all order-related endpoints
public class OrderController {

    @Autowired
    private OrderService orderService; // Service for order operations

    @Autowired
    private UserService userService; // Service for user-related operations

    /**
     * Places an order for the given customer ID.
     *
     * @param customerId the ID of the customer placing the order.
     * @return ResponseEntity containing the created order or an error message.
     */
    @PostMapping("/place")
    public ResponseEntity<?> placeOrder(@RequestParam Long customerId) {
        try {
            Orders order = orderService.placeOrder(customerId); // Attempt to place order
            return new ResponseEntity<>(order, HttpStatus.CREATED); // Success response
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST); // Error response
        }
    }

    /**
     * Retrieves all orders placed by a specific customer.
     *
     * @param customerId the ID of the customer.
     * @return list of Orders placed by the customer.
     */
    @GetMapping("/customer/{customerId}")
    public List<Orders> getOrdersByCustomer(@PathVariable Long customerId) {
        return orderService.getOrdersByCustomer(customerId);
    }

    /**
     * Retrieves an order by its ID.
     *
     * @param orderId the ID of the order.
     * @return the Order object corresponding to the given ID.
     */
    @GetMapping("/{orderId}")
    public Orders getOrderById(@PathVariable Long orderId) {
        return orderService.getOrderById(orderId);
    }

    /**
     * Retrieves all orders related to restaurants owned by the given owner.
     *
     * @param ownerId the ID of the restaurant owner.
     * @return ResponseEntity containing list of Orders or an error message.
     */
    @GetMapping("/owner/{ownerId}")
    public ResponseEntity<?> getOrdersByOwner(@PathVariable Long ownerId) {
        try {
            List<Orders> orders = orderService.getOrdersByOwner(ownerId); // Fetch orders by owner
            return new ResponseEntity<>(orders, HttpStatus.OK); // Success response
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST); // Error response
        }
    }
}
