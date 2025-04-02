package com.example.restaurantmanagement.service.impl;

//import com.example.restaurantmanagement.model.User;
import com.example.restaurantmanagement.model.app_user;
import com.example.restaurantmanagement.repository.UserRepository;
import com.example.restaurantmanagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public app_user registerUser(app_user user) {
        return userRepository.save(user);
    }

    @Override
    public app_user loginUser(String email, String password) {
        app_user user = userRepository.findByEmail(email);
        if(user != null && user.getPassword().equals(password)) {
            return user;
        }
        throw new RuntimeException("Invalid credentials");
    }

    @Override
    public app_user getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    public app_user getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public double getWalletBalance(Long customerId) {
        app_user user = getUserById(customerId);
        return user.getWalletBalance() != null ? user.getWalletBalance() : 0;
    }

    @Override
    public void updateWalletBalance(Long customerId, double newBalance) {
        app_user user = getUserById(customerId);
        user.setWalletBalance(newBalance);
        userRepository.save(user);
    }
}
