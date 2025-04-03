package com.example.restaurantmanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TopRestaurantDTO {
    private Long restaurantId;
    private String name;
    private Long orderCount;
}
