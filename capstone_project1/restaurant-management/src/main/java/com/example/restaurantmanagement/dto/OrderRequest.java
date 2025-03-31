package com.example.restaurantmanagement.dto;

import lombok.Data;
import java.util.List;

@Data
public class OrderRequest {
    private Long restaurantId;
    private List<OrderItemRequest> items;

    @Data
    public static class OrderItemRequest {
        private Long menuItemId;
        private int quantity;
    }
}
