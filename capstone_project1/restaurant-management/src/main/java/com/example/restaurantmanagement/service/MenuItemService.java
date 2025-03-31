package com.example.restaurantmanagement.service;

import com.example.restaurantmanagement.model.MenuItem;
import java.util.List;

public interface MenuItemService {
    MenuItem addMenuItem(MenuItem menuItem);
    MenuItem updateMenuItem(MenuItem menuItem);
    void deleteMenuItem(Long id);
    List<MenuItem> searchMenuItems(String name);
    List<MenuItem> getMenuItemsByRestaurant(Long restaurantId);
    MenuItem getMenuItemById(Long id);
}
