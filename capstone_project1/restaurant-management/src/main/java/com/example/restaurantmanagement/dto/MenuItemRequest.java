package com.example.restaurantmanagement.dto;

import lombok.Data;

@Data
public class MenuItemRequest {
    private String name;
    private String description;
    private double price;
    private String imageUrl;
}
