package com.example.restaurantmanagement.controller;

import com.example.restaurantmanagement.model.Restaurant;
import com.example.restaurantmanagement.service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/restaurants") // Base URL for restaurant-related operations
public class RestaurantController {

    @Autowired
    private RestaurantService restaurantService; // Injects RestaurantService for business logic

    /**
     * Retrieves all restaurants.
     *
     * @return list of all restaurants
     */
    @GetMapping("/all")
    public List<Restaurant> getAllRestaurants() {
        return restaurantService.getAllRestaurants();
    }

    /**
     * Retrieves a specific restaurant by its ID.
     *
     * @param id ID of the restaurant
     * @return the Restaurant object
     */
    @GetMapping("/{id}")
    public Restaurant getRestaurantById(@PathVariable Long id) {
        return restaurantService.getRestaurantById(id);
    }

    /**
     * Retrieves all restaurants owned by a specific owner.
     *
     * @param ownerId ID of the owner
     * @return list of restaurants owned by the given owner
     */
    @GetMapping("/owner/{ownerId}")
    public List<Restaurant> getRestaurantsByOwner(@PathVariable Long ownerId) {
        return restaurantService.getRestaurantsByOwnerId(ownerId);
    }

    /**
     * Adds a new restaurant.
     *
     * @param restaurant the Restaurant object to be added
     * @return the saved Restaurant object
     */
    @PostMapping("/add")
    public Restaurant addRestaurant(@RequestBody Restaurant restaurant) {
        return restaurantService.addRestaurant(restaurant);
    }

    /**
     * Updates an existing restaurant.
     *
     * @param id ID of the restaurant to update
     * @param restaurant updated Restaurant object
     * @return the updated Restaurant object
     */
    @PutMapping("/{id}")
    public Restaurant updateRestaurant(@PathVariable Long id, @RequestBody Restaurant restaurant) {
        restaurant.setId(id); // Ensure the ID is set from path
        return restaurantService.updateRestaurant(restaurant);
    }

    /**
     * Deletes a restaurant by ID.
     *
     * @param id ID of the restaurant to delete
     */
    @DeleteMapping("/{id}")
    public void deleteRestaurant(@PathVariable Long id) {
        restaurantService.deleteRestaurant(id);
    }
}
