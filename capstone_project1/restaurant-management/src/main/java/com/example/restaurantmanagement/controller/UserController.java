package com.example.restaurantmanagement.controller;

import com.example.restaurantmanagement.model.app_user;
import com.example.restaurantmanagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for managing user-related operations.
 */
@RestController
@RequestMapping("/api/users") // Base endpoint for user operations
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * Retrieves a user by their ID.
     *
     * @param id the ID of the user
     * @return the user corresponding to the provided ID
     */
    @GetMapping("/{id}")
    public app_user getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    /**
     * Retrieves a user by their email address.
     *
     * @param email the email address of the user
     * @return the user corresponding to the provided email
     */
    @GetMapping("/by-email")
    public app_user getUserByEmail(@RequestParam String email) {
        return userService.getUserByEmail(email);
    }
}
