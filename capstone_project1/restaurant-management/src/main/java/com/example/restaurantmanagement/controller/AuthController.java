package com.example.restaurantmanagement.controller;

import com.example.restaurantmanagement.model.app_user;
import com.example.restaurantmanagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * This controller handles authentication-related endpoints
 * such as user registration and login.
 */
@RestController
@RequestMapping("/api/auth") // Base URL for all authentication routes
public class AuthController {

    @Autowired
    private UserService userService; // Injecting the UserService to handle user-related business logic

    /**
     * Handles the user registration request.
     * URL: POST /api/auth/register
     *
     * @param user the user details received in the request body
     * @return the newly registered user object
     */
    @PostMapping("/register")
    public app_user register(@RequestBody app_user user) {
        return userService.registerUser(user); // Delegates user registration to the service layer
    }

    /**
     * Handles the user login request.
     * URL: POST /api/auth/login
     *
     * @param user the user credentials received in the request body
     * @return the user object if login is successful
     */
    @PostMapping("/login")
    public app_user login(@RequestBody app_user user) {
        return userService.loginUser(user.getEmail(), user.getPassword()); // Validates credentials and returns user
    }

}
