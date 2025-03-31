package com.example.restaurantmanagement.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "customer_order")
@Data
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Customer who placed the order
    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private User customer;

    // Restaurant from which the order is placed
    @ManyToOne
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;

    @Column(nullable = false)
    private double total;

    @Column(nullable = false)
    private String status; // e.g., "Pending", "Completed", "Cancelled"

    private LocalDateTime orderDate = LocalDateTime.now();

    // Wallet balance remaining after order
    @Transient
    private Double remainingWalletBalance;

    // Optional order details, if needed:
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<OrderDetail> orderDetails;
}
