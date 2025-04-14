package com.example.restaurantmanagement.service.impl;

import com.example.restaurantmanagement.model.app_user;
import com.example.restaurantmanagement.repository.UserRepository;
import com.example.restaurantmanagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementation of the UserService interface to manage user operations such as registration, login, and wallet management.
 * This class handles user-related tasks like registration, login authentication, and updating wallet balance.
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    /**
     * Registers a new user by saving their details in the database.
     *
     * @param user the user object containing the registration details
     * @return the registered user with a generated ID
     */
    @Override
    public app_user registerUser(app_user user) {
        return userRepository.save(user);
    }

    /**
     * Authenticates a user by checking their email and password.
     *
     * @param email the email address of the user attempting to log in
     * @param password the password of the user attempting to log in
     * @return the authenticated user if credentials are valid
     * @throws RuntimeException if the credentials are invalid
     */
    @Override
    public app_user loginUser(String email, String password) {
        app_user user = userRepository.findByEmail(email);
        if(user != null && user.getPassword().equals(password)) {
            return user;
        }
        throw new RuntimeException("Invalid credentials");
    }

    /**
     * Retrieves a user by their ID.
     *
     * @param id the ID of the user
     * @return the user with the specified ID
     * @throws RuntimeException if the user with the given ID is not found
     */
    @Override
    public app_user getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    /**
     * Retrieves a user by their email address.
     *
     * @param email the email address of the user
     * @return the user with the specified email
     */
    @Override
    public app_user getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    /**
     * Retrieves the wallet balance of a customer.
     *
     * @param customerId the ID of the customer
     * @return the wallet balance of the customer, defaulting to 0 if no balance is found
     */
    @Override
    public double getWalletBalance(Long customerId) {
        app_user user = getUserById(customerId);
        return user.getWalletBalance() != null ? user.getWalletBalance() : 0;
    }

    /**
     * Updates the wallet balance of a customer.
     *
     * @param customerId the ID of the customer whose wallet balance is to be updated
     * @param newBalance the new wallet balance to set
     */
    @Override
    public void updateWalletBalance(Long customerId, double newBalance) {
        app_user user = getUserById(customerId);
        user.setWalletBalance(newBalance);
        userRepository.save(user);
    }
}
