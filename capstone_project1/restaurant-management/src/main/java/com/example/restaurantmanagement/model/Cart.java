package com.example.restaurantmanagement.model;

import jakarta.persistence.*;
import lombok.Data;

/**
 * Entity representing a shopping cart item for a customer.
 * It stores the relationship between the customer, menu item, and quantity.
 */

@Entity
@Data
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private app_user customer;

    @ManyToOne
    @JoinColumn(name = "menu_item_id", nullable = false)
    private MenuItem menuItem;

    @Column(nullable = false)
    private int quantity;
}
