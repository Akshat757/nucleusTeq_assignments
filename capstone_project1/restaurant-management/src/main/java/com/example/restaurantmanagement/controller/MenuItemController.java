package com.example.restaurantmanagement.controller;

import com.example.restaurantmanagement.model.MenuItem;
import com.example.restaurantmanagement.service.MenuItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/menuitems") // Base path for all menu item-related endpoints
public class MenuItemController {

    @Autowired
    private MenuItemService menuItemService; // Service layer for menu item operations

    /**
     * Retrieves all menu items.
     * If no search query is provided, returns all menu items.
     *
     * @return list of all MenuItem objects.
     */
    @GetMapping
    public List<MenuItem> getAllMenuItems() {
        return menuItemService.searchMenuItems(""); // returns all if query is empty
    }

    /**
     * Retrieves a specific menu item by its ID.
     *
     * @param id the ID of the menu item.
     * @return the MenuItem with the given ID.
     */
    @GetMapping("/{id}")
    public MenuItem getMenuItemById(@PathVariable Long id) {
        return menuItemService.getMenuItemById(id);
    }

    /**
     * Retrieves all menu items for a given restaurant.
     *
     * @param restaurantId the ID of the restaurant.
     * @return list of MenuItem objects for the specified restaurant.
     */
    @GetMapping("/restaurant/{restaurantId}")
    public List<MenuItem> getMenuItemsByRestaurant(@PathVariable Long restaurantId) {
        return menuItemService.getMenuItemsByRestaurant(restaurantId);
    }

    /**
     * Adds a new menu item.
     *
     * @param menuItem the MenuItem object to be added.
     * @return the newly added MenuItem.
     */
    @PostMapping
    public MenuItem addMenuItem(@RequestBody MenuItem menuItem) {
        return menuItemService.addMenuItem(menuItem);
    }

    /**
     * Updates an existing menu item by its ID.
     *
     * @param id the ID of the menu item to update.
     * @param menuItem the updated MenuItem object.
     * @return the updated MenuItem.
     */
    @PutMapping("/{id}")
    public MenuItem updateMenuItem(@PathVariable Long id, @RequestBody MenuItem menuItem) {
        menuItem.setId(id); // Ensure the menu item has the correct ID
        return menuItemService.updateMenuItem(menuItem);
    }

    /**
     * Deletes a menu item by its ID.
     *
     * @param id the ID of the menu item to delete.
     */
    @DeleteMapping("/{id}")
    public void deleteMenuItem(@PathVariable Long id) {
        menuItemService.deleteMenuItem(id);
    }
}
