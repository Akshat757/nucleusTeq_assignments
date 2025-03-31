package com.example.restaurantmanagement.dto;

import lombok.Data;

@Data
public class CartDTO {
    private Long customerId;
    private Long itemId;
    private int quantity;
}
