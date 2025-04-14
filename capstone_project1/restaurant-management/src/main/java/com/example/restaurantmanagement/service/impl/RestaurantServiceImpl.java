package com.example.restaurantmanagement.service.impl;

import com.example.restaurantmanagement.model.Restaurant;
import com.example.restaurantmanagement.repository.RestaurantRepository;
import com.example.restaurantmanagement.service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * Implementation of the RestaurantService interface to manage restaurant operations.
 * This class handles adding, updating, deleting, and retrieving restaurant information.
 */
@Service
public class RestaurantServiceImpl implements RestaurantService {

    @Autowired
    private RestaurantRepository restaurantRepository;

    /**
     * Adds a new restaurant to the system.
     *
     * @param restaurant the restaurant object to be added
     * @return the added restaurant with generated ID
     */
    @Override
    public Restaurant addRestaurant(Restaurant restaurant) {
        return restaurantRepository.save(restaurant);
    }

    /**
     * Updates an existing restaurant's details.
     *
     * @param restaurant the restaurant object with updated information
     * @return the updated restaurant
     */
    @Override
    public Restaurant updateRestaurant(Restaurant restaurant) {
        return restaurantRepository.save(restaurant);
    }

    /**
     * Deletes a restaurant by its ID.
     *
     * @param id the ID of the restaurant to be deleted
     */
    @Override
    public void deleteRestaurant(Long id) {
        restaurantRepository.deleteById(id);
    }

    /**
     * Searches for restaurants by their name.
     *
     * @param name the name or part of the name of the restaurant to search for
     * @return a list of restaurants whose name contains the provided search term
     */
    @Override
    public List<Restaurant> searchRestaurants(String name) {
        return restaurantRepository.findByNameContainingIgnoreCase(name);
    }

    /**
     * Retrieves a list of restaurants owned by a specific owner.
     *
     * @param ownerId the ID of the restaurant owner
     * @return a list of restaurants owned by the specified owner
     */
    @Override
    public List<Restaurant> getRestaurantsByOwnerId(Long ownerId) {
        return restaurantRepository.findByOwner_Id(ownerId);
    }

    /**
     * Retrieves a list of all restaurants in the system.
     *
     * @return a list of all restaurants
     */
    @Override
    public List<Restaurant> getAllRestaurants() {
        return restaurantRepository.findAll();
    }

    /**
     * Retrieves a restaurant by its ID.
     *
     * @param id the ID of the restaurant
     * @return the restaurant with the specified ID
     * @throws RuntimeException if the restaurant with the given ID is not found
     */
    @Override
    public Restaurant getRestaurantById(Long id) {
        return restaurantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));
    }
}
