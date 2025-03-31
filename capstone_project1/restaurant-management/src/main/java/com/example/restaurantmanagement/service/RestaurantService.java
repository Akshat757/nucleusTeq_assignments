package com.example.restaurantmanagement.service;

import com.example.restaurantmanagement.model.Restaurant;
import java.util.List;

public interface RestaurantService {
    Restaurant addRestaurant(Restaurant restaurant);
    Restaurant updateRestaurant(Restaurant restaurant);
    void deleteRestaurant(Long id);
    List<Restaurant> searchRestaurants(String name);
    List<Restaurant> getRestaurantsByOwnerId(Long ownerId);
    List<Restaurant> getAllRestaurants();
    Restaurant getRestaurantById(Long id);
}
