package com.example.restaurantmanagement.dto;

import lombok.Data;

@Data
public class CartUpdateRequest {
    private Long cartId;
    private int quantity;
}
