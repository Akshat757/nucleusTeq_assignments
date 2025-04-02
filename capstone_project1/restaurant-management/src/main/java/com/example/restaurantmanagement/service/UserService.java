package com.example.restaurantmanagement.service;

//import com.example.restaurantmanagement.model.User;
import com.example.restaurantmanagement.model.app_user;

public interface UserService {
    app_user registerUser(app_user user);
    app_user loginUser(String email, String password);
    app_user getUserById(Long id);
    app_user getUserByEmail(String email);
    double getWalletBalance(Long customerId);
    void updateWalletBalance(Long customerId, double newBalance);
}
