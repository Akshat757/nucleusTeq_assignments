package com.example.restaurantmanagement.dto;

import lombok.Data;

@Data
public class RestaurantResponseDTO {
    private Long id;
    private String name;
    private String location;
    private String imageUrl;
    // Omit menuItems field
}
