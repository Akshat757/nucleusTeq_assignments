package com.example.restaurantmanagement.service;

import com.example.restaurantmanagement.model.User;

public interface UserService {
    User registerUser(User user);
    User loginUser(String email, String password);
    User getUserById(Long id);
    User getUserByEmail(String email);
    double getWalletBalance(Long customerId);
    void updateWalletBalance(Long customerId, double newBalance);
}
