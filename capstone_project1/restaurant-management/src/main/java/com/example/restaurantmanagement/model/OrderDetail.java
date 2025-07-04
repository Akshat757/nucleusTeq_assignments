package com.example.restaurantmanagement.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * Entity representing the details of an order.
 * It links each order with its respective menu items, quantity, and price.
 */
@Entity
@Getter
@Setter
@Data
public class OrderDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false, referencedColumnName = "id")
    @JsonBackReference
    private Orders order;

    @ManyToOne
    private MenuItem menuItem;

    private int quantity;
    private double price;
}
