package com.example.restaurantmanagement.controller;

import com.example.restaurantmanagement.dto.TopRestaurantDTO;
import com.example.restaurantmanagement.model.Orders;
import com.example.restaurantmanagement.model.Restaurant;
import com.example.restaurantmanagement.repository.OrderRepository;
import com.example.restaurantmanagement.repository.RestaurantRepository;
import com.example.restaurantmanagement.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/owner/dashboard") // Base path for owner dashboard operations
public class OwnerDashboardController {

    @Autowired
    private RestaurantRepository restaurantRepository; // Repository to fetch restaurant data

    @Autowired
    private OrderRepository orderRepository; // Repository to fetch order data

    @Autowired
    private OrderService orderService; // Service for order-related business logic

    /**
     * Endpoint to retrieve a summary of restaurants for a given owner.
     *
     * @param ownerId ID of the restaurant owner
     * @return summary containing total restaurants and a sorted list of top restaurants
     */
    @GetMapping("/restaurants")
    public ResponseEntity<?> getRestaurantSummary(@RequestParam Long ownerId) {
        // Fetch all restaurants owned by the specified owner
        List<Restaurant> restaurants = restaurantRepository.findByOwner_Id(ownerId);
        int totalRestaurants = restaurants.size(); // Count total restaurants

        // Sort restaurants alphabetically by name
        List<Restaurant> topRestaurants = restaurants.stream()
                .sorted((r1, r2) -> r1.getName().compareToIgnoreCase(r2.getName()))
                .collect(Collectors.toList());

        // Prepare response summary
        Map<String, Object> summary = new HashMap<>();
        summary.put("totalRestaurants", totalRestaurants);
        summary.put("topRestaurants", topRestaurants);

        return ResponseEntity.ok(summary);
    }

    /**
     * Endpoint to retrieve an orders summary for a given owner.
     *
     * @param ownerId ID of the restaurant owner
     * @return summary containing total orders, total revenue, and recent orders
     */
    @GetMapping("/orders")
    public ResponseEntity<?> getOrdersSummary(@RequestParam Long ownerId) {
        // Fetch all orders associated with the owner's restaurants
        List<Orders> orders = orderRepository.findByRestaurantOwnerId(ownerId);
        int totalOrders = orders.size(); // Total number of orders
        double totalRevenue = orders.stream()
                .mapToDouble(Orders::getTotalPrice)
                .sum(); // Calculate total revenue

        // Get the 5 most recent orders sorted by order date descending
        List<Orders> recentOrders = orders.stream()
                .sorted((o1, o2) -> o2.getOrderDate().compareTo(o1.getOrderDate()))
                .limit(5)
                .collect(Collectors.toList());

        // Prepare response summary
        Map<String, Object> summary = new HashMap<>();
        summary.put("totalOrders", totalOrders);
        summary.put("totalRevenue", totalRevenue);
        summary.put("recentOrders", recentOrders);

        return ResponseEntity.ok(summary);
    }

    /**
     * Endpoint to get top performing restaurants of an owner based on orders.
     *
     * @param ownerId ID of the restaurant owner
     * @return list of TopRestaurantDTO containing top restaurants info
     */
    @GetMapping("/top-restaurants")
    public ResponseEntity<List<TopRestaurantDTO>> getTopRestaurants(@RequestParam Long ownerId) {
        List<TopRestaurantDTO> topRestaurants = orderService.getTopRestaurantsByOwner(ownerId);
        return ResponseEntity.ok(topRestaurants);
    }

}
