package com.example.restaurantmanagement.controller;

import com.example.restaurantmanagement.dto.CartDTO;
import com.example.restaurantmanagement.model.Cart;
import com.example.restaurantmanagement.model.MenuItem;
import com.example.restaurantmanagement.model.User;
import com.example.restaurantmanagement.service.CartService;
import com.example.restaurantmanagement.service.MenuItemService;
import com.example.restaurantmanagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private UserService userService;

    @Autowired
    private MenuItemService menuItemService;

    @PostMapping("/add")
    public Cart addCartItem(@RequestBody CartDTO cartDTO) {
        // Load the customer and menu item from the database
        User customer = userService.getUserById(cartDTO.getCustomerId());
        if (customer == null) {
            throw new RuntimeException("Customer with ID " + cartDTO.getCustomerId() + " not found.");
        }
        MenuItem menuItem = menuItemService.getMenuItemById(cartDTO.getItemId());
        if (menuItem == null) {
            throw new RuntimeException("Menu item with ID " + cartDTO.getItemId() + " not found.");
        }

        // Create and populate a Cart entity
        Cart cart = new Cart();
        cart.setCustomer(customer);
        cart.setMenuItem(menuItem);
        cart.setQuantity(cartDTO.getQuantity());

        return cartService.addCartItem(cart);
    }

    @GetMapping("/customer/{customerId}")
    public List<Cart> getCartItemsByCustomer(@PathVariable Long customerId) {
        return cartService.getCartItemsByCustomer(customerId);
    }

    @DeleteMapping("/remove/{id}")
    public void deleteCartItem(@PathVariable Long id) {
        cartService.deleteCartItem(id);
    }
}
