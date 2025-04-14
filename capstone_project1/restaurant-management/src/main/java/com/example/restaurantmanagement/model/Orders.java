package com.example.restaurantmanagement.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Entity representing an order placed by a customer in a restaurant.
 * It includes details about the customer, restaurant, total amount, order status, and order items.
 */

@Entity
@Data
public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Customer who placed the order
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private app_user customer;

    // Restaurant from which the order is placed
    @ManyToOne
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;

    @Column(nullable = false)
    private double total;

    @Column(name = "total_price", nullable = false)
    private double totalPrice;

    @Column(nullable = false)
    private String status; // e.g., "Pending", "Completed", "Cancelled"

    private LocalDateTime orderDate = LocalDateTime.now();

    // Computed getter for total price (converted to BigDecimal)
    public Double getTotalPrice() {
        return total;
    }

    // Wallet balance remaining after order
    @Transient
    private Double remainingWalletBalance;

    // order details, if needed:
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<OrderDetail> orderDetails;
}
