package com.example.restaurantmanagement.controller;

import com.example.restaurantmanagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for handling wallet-related operations such as retrieving
 * the wallet balance of a customer.
 */
@RestController
@RequestMapping("/api/wallet") // Base endpoint for wallet-related operations
public class WalletController {

    @Autowired
    private UserService userService;

    /**
     * Retrieves the wallet balance for a specific customer.
     *
     * @param customerId the ID of the customer
     * @return the wallet balance of the customer
     */
    @GetMapping("/customer/{customerId}")
    public double getWalletBalance(@PathVariable Long customerId) {
        return userService.getWalletBalance(customerId);
    }
}
