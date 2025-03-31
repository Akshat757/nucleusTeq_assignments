package com.example.restaurantmanagement.dto;

import lombok.Data;

@Data
public class SignUpRequest {
    private String email;
    private String password;
    private String role; // CUSTOMER or OWNER
}
