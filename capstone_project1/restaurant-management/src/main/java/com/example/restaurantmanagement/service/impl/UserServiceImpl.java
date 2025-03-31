package com.example.restaurantmanagement.service.impl;

import com.example.restaurantmanagement.model.User;
import com.example.restaurantmanagement.repository.UserRepository;
import com.example.restaurantmanagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User registerUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public User loginUser(String email, String password) {
        User user = userRepository.findByEmail(email);
        if(user != null && user.getPassword().equals(password)) {
            return user;
        }
        throw new RuntimeException("Invalid credentials");
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public double getWalletBalance(Long customerId) {
        User user = getUserById(customerId);
        return user.getWalletBalance() != null ? user.getWalletBalance() : 0;
    }

    @Override
    public void updateWalletBalance(Long customerId, double newBalance) {
        User user = getUserById(customerId);
        user.setWalletBalance(newBalance);
        userRepository.save(user);
    }
}
