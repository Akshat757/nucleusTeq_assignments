package com.example.restaurantmanagement.controller;

import com.example.restaurantmanagement.dto.CartDTO;
import com.example.restaurantmanagement.dto.CartUpdateRequest;
import com.example.restaurantmanagement.model.Cart;
import com.example.restaurantmanagement.model.MenuItem;
//import com.example.restaurantmanagement.model.User;
import com.example.restaurantmanagement.model.app_user;
import com.example.restaurantmanagement.service.CartService;
import com.example.restaurantmanagement.service.MenuItemService;
import com.example.restaurantmanagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

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
        app_user customer = userService.getUserById(cartDTO.getCustomerId());
        if (customer == null) {
            throw new RuntimeException("Customer with ID " + cartDTO.getCustomerId() + " not found.");
        }
        MenuItem menuItem = menuItemService.getMenuItemById(cartDTO.getItemId());
        if (menuItem == null) {
            throw new RuntimeException("Menu item with ID " + cartDTO.getItemId() + " not found.");
        }

        Cart cart = new Cart();
        cart.setCustomer(customer);
        cart.setMenuItem(menuItem);
        cart.setQuantity(cartDTO.getQuantity());

        return cartService.addCartItem(cart);
    }

    @PutMapping("/update/{cartId}")
    public ResponseEntity<String> updateCartItem(@PathVariable Long cartId, @RequestBody Map<String, Integer> request) {
        int quantity = request.get("quantity");
        cartService.updateCartQuantity(cartId, quantity);
        return ResponseEntity.ok("Cart item updated successfully");
    }

    @GetMapping("/customer/{customerId}")
    public List<Cart> getCartItemsByCustomer(@PathVariable Long customerId) {
        return cartService.getCartItemsByCustomer(customerId);
    }

    @DeleteMapping("/remove/{id}")
    public void deleteCartItem(@PathVariable Long id) {
        cartService.deleteCartItem(id);
    }

    // ✅ Get all cart items
    @GetMapping("/all")
    public List<Cart> getAllCartItems() {
        return cartService.getAllCartItems();
    }

    // ✅ Get cart item by ID
    @GetMapping("/{cartId}")
    public Cart getCartItemById(@PathVariable Long cartId) {
        return cartService.getCartItemById(cartId);
    }

    // ✅ Update multiple cart items
    @PutMapping("/update")
    public ResponseEntity<String> updateCartItems(@RequestBody List<CartUpdateRequest> cartUpdates) {
        cartService.updateCartItems(cartUpdates);
        return ResponseEntity.ok("Cart items updated successfully");
    }
}
