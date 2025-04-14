package com.example.restaurantmanagement.controller;

import com.example.restaurantmanagement.dto.CartDTO;
import com.example.restaurantmanagement.dto.CartUpdateRequest;
import com.example.restaurantmanagement.model.Cart;
import com.example.restaurantmanagement.model.MenuItem;
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
@RequestMapping("/api/cart") // Base path for all cart-related operations
public class CartController {

    @Autowired
    private CartService cartService; // Service to handle cart operations

    @Autowired
    private UserService userService; // Service to handle user data

    @Autowired
    private MenuItemService menuItemService; // Service to handle menu item data

    /**
     * Adds an item to the cart for a specific customer.
     *
     * @param cartDTO DTO containing customer ID, menu item ID, and quantity.
     * @return the added Cart object.
     */
    @PostMapping("/add")
    public Cart addCartItem(@RequestBody CartDTO cartDTO) {
        // Fetch the customer based on customerId
        app_user customer = userService.getUserById(cartDTO.getCustomerId());
        if (customer == null) {
            throw new RuntimeException("Customer with ID " + cartDTO.getCustomerId() + " not found.");
        }

        // Fetch the menu item based on itemId
        MenuItem menuItem = menuItemService.getMenuItemById(cartDTO.getItemId());
        if (menuItem == null) {
            throw new RuntimeException("Menu item with ID " + cartDTO.getItemId() + " not found.");
        }

        // Create a new Cart object and set values
        Cart cart = new Cart();
        cart.setCustomer(customer);
        cart.setMenuItem(menuItem);
        cart.setQuantity(cartDTO.getQuantity());

        // Save and return the cart item
        return cartService.addCartItem(cart);
    }

    /**
     * Updates the quantity of a specific cart item.
     *
     * @param cartId  the ID of the cart item to update.
     * @param request map containing the new quantity value.
     * @return response indicating success or failure.
     */
    @PutMapping("/update/{cartId}")
    public ResponseEntity<String> updateCartItem(@PathVariable Long cartId, @RequestBody Map<String, Integer> request) {
        int quantity = request.get("quantity"); // Extract new quantity from request body
        cartService.updateCartQuantity(cartId, quantity); // Call service to update
        return ResponseEntity.ok("Cart item updated successfully"); // Return success message
    }

    /**
     * Retrieves all cart items for a specific customer.
     *
     * @param customerId ID of the customer.
     * @return list of Cart items.
     */
    @GetMapping("/customer/{customerId}")
    public List<Cart> getCartItemsByCustomer(@PathVariable Long customerId) {
        return cartService.getCartItemsByCustomer(customerId);
    }

    /**
     * Deletes a cart item based on its ID.
     *
     * @param id ID of the cart item to delete.
     */
    @DeleteMapping("/remove/{id}")
    public void deleteCartItem(@PathVariable Long id) {
        cartService.deleteCartItem(id);
    }

    /**
     * ✅ Retrieves all cart items in the system.
     *
     * @return list of all Cart items.
     */
    @GetMapping("/all")
    public List<Cart> getAllCartItems() {
        return cartService.getAllCartItems();
    }

    /**
     * ✅ Retrieves a cart item by its ID.
     *
     * @param cartId ID of the cart item.
     * @return the corresponding Cart object.
     */
    @GetMapping("/{cartId}")
    public Cart getCartItemById(@PathVariable Long cartId) {
        return cartService.getCartItemById(cartId);
    }

    /**
     * ✅ Updates multiple cart items at once.
     *
     * @param cartUpdates list of cart update requests containing cart IDs and new quantities.
     * @return response indicating success.
     */
    @PutMapping("/update")
    public ResponseEntity<String> updateCartItems(@RequestBody List<CartUpdateRequest> cartUpdates) {
        cartService.updateCartItems(cartUpdates); // Batch update via service
        return ResponseEntity.ok("Cart items updated successfully");
    }
}
