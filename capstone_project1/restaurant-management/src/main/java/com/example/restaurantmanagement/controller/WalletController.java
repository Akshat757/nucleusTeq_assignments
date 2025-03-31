package com.example.restaurantmanagement.controller;

import com.example.restaurantmanagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/wallet")
public class WalletController {

    @Autowired
    private UserService userService;

    @GetMapping("/customer/{customerId}")
    public double getWalletBalance(@PathVariable Long customerId) {
        return userService.getWalletBalance(customerId);
    }
}
