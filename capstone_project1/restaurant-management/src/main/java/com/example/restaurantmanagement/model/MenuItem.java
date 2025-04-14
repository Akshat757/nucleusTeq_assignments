package com.example.restaurantmanagement.model;

import jakarta.persistence.*;
import lombok.Data;

/**
 * Entity representing a menu item in the system.
 * It stores the details of each menu item like name, description, price, and associated restaurant.
 */

@Entity
@Data
public class MenuItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String description;

    @Column(nullable = false)
    private double price;

    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;
}
