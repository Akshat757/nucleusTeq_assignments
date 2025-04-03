package com.example.restaurantmanagement.controller;

import com.example.restaurantmanagement.model.Orders;
import com.example.restaurantmanagement.model.Restaurant;
import com.example.restaurantmanagement.repository.OrderRepository;
import com.example.restaurantmanagement.repository.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/owner/dashboard")
public class OwnerDashboardController {

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private OrderRepository orderRepository;

    // Endpoint to get restaurant summary for an owner
    @GetMapping("/restaurants")
    public ResponseEntity<?> getRestaurantSummary(@RequestParam Long ownerId) {
        // Fetch restaurants for the owner
        List<Restaurant> restaurants = restaurantRepository.findByOwner_Id(ownerId);
        int totalRestaurants = restaurants.size();

        // (Optional) Calculate top-performing restaurants if needed
        // For now, we return the list of restaurants sorted by name.
        List<Restaurant> topRestaurants = restaurants.stream()
                .sorted((r1, r2) -> r1.getName().compareToIgnoreCase(r2.getName()))
                .collect(Collectors.toList());

        Map<String, Object> summary = new HashMap<>();
        summary.put("totalRestaurants", totalRestaurants);
        summary.put("topRestaurants", topRestaurants);

        return ResponseEntity.ok(summary);
    }

    // Endpoint to get orders summary for an owner
    @GetMapping("/orders")
    public ResponseEntity<?> getOrdersSummary(@RequestParam Long ownerId) {
        // Fetch orders for restaurants owned by the given owner
        List<Orders> orders = orderRepository.findByRestaurantOwnerId(ownerId);
        int totalOrders = orders.size();
        double totalRevenue = orders.stream()
                .mapToDouble(Orders::getTotalPrice)
                .sum();

        // Sort orders by order date descending and limit to 5 recent orders
        List<Orders> recentOrders = orders.stream()
                .sorted((o1, o2) -> o2.getOrderDate().compareTo(o1.getOrderDate()))
                .limit(5)
                .collect(Collectors.toList());

        Map<String, Object> summary = new HashMap<>();
        summary.put("totalOrders", totalOrders);
        summary.put("totalRevenue", totalRevenue);
        summary.put("recentOrders", recentOrders);

        return ResponseEntity.ok(summary);
    }
}
