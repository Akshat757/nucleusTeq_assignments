package com.example.restaurantmanagement.service.impl;

import com.example.restaurantmanagement.model.MenuItem;
import com.example.restaurantmanagement.repository.MenuItemRepository;
import com.example.restaurantmanagement.service.MenuItemService;
import com.example.restaurantmanagement.service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * Implementation of the MenuItemService interface for managing menu items in the system.
 * This class provides methods for adding, updating, deleting, and retrieving menu items.
 */
@Service
public class MenuItemServiceImpl implements MenuItemService {

    @Autowired
    private MenuItemRepository menuItemRepository;

    @Autowired
    private RestaurantService restaurantService;

    /**
     * Adds a new menu item to the system.
     *
     * @param menuItem the menu item to be added
     * @return the saved menu item
     */
    @Override
    public MenuItem addMenuItem(MenuItem menuItem) {
        return menuItemRepository.save(menuItem);
    }

    /**
     * Updates an existing menu item.
     *
     * @param menuItem the menu item with updated details
     * @return the updated menu item
     */
    @Override
    public MenuItem updateMenuItem(MenuItem menuItem) {
        return menuItemRepository.save(menuItem);
    }

    /**
     * Deletes a menu item from the system.
     *
     * @param id the ID of the menu item to be deleted
     */
    @Override
    public void deleteMenuItem(Long id) {
        menuItemRepository.deleteById(id);
    }

    /**
     * Searches for menu items by name, ignoring case.
     *
     * @param name the name of the menu item to search for
     * @return a list of menu items whose names contain the search string
     */
    @Override
    public List<MenuItem> searchMenuItems(String name) {
        return menuItemRepository.findByNameContainingIgnoreCase(name);
    }

    /**
     * Retrieves all menu items for a specific restaurant.
     *
     * @param restaurantId the ID of the restaurant
     * @return a list of menu items for the specified restaurant
     */
    @Override
    public List<MenuItem> getMenuItemsByRestaurant(Long restaurantId) {
        return menuItemRepository.findByRestaurant_Id(restaurantId);
    }

    /**
     * Retrieves a menu item by its ID.
     *
     * @param id the ID of the menu item
     * @return the menu item if found
     * @throws RuntimeException if the menu item is not found
     */
    @Override
    public MenuItem getMenuItemById(Long id) {
        return menuItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Menu item not found"));
    }
}
