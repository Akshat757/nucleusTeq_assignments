package com.example.restaurantmanagement.service.impl;

import com.example.restaurantmanagement.dto.CartUpdateRequest;
import com.example.restaurantmanagement.model.Cart;
import com.example.restaurantmanagement.repository.CartRepository;
import com.example.restaurantmanagement.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * Implementation of the CartService interface for managing cart items.
 * This class provides methods for adding, updating, deleting, and retrieving cart items.
 */
@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartRepository cartRepository;

    /**
     * Adds a new item to the cart or updates the quantity if the item already exists in the cart.
     *
     * @param cart the cart item to be added
     * @return the updated cart item
     */
    @Override
    public Cart addCartItem(Cart cart) {
        Cart existing = cartRepository.findByCustomer_IdAndMenuItem_Id(
                cart.getCustomer().getId(), cart.getMenuItem().getId());
        if (existing != null) {
            existing.setQuantity(existing.getQuantity() + cart.getQuantity());
            return cartRepository.save(existing);
        }
        return cartRepository.save(cart);
    }

    /**
     * Updates the quantity or details of an existing cart item.
     *
     * @param cart the cart item with updated details
     * @return the updated cart item
     */
    @Override
    public Cart updateCartItem(Cart cart) {
        return cartRepository.save(cart);
    }

    /**
     * Deletes a cart item by its ID.
     *
     * @param id the ID of the cart item to be deleted
     */
    @Override
    public void deleteCartItem(Long id) {
        cartRepository.deleteById(id);
    }

    /**
     * Retrieves all cart items for a given customer.
     *
     * @param customerId the ID of the customer whose cart items are to be retrieved
     * @return a list of cart items belonging to the customer
     */
    @Override
    public List<Cart> getCartItemsByCustomer(Long customerId) {
        return cartRepository.findByCustomer_Id(customerId);
    }

    /**
     * Retrieves a specific cart item by customer ID and menu item ID.
     *
     * @param customerId the ID of the customer
     * @param menuItemId the ID of the menu item
     * @return the cart item if found
     */
    @Override
    public Cart getCartItemByCustomerAndMenuItem(Long customerId, Long menuItemId) {
        return cartRepository.findByCustomer_IdAndMenuItem_Id(customerId, menuItemId);
    }

    /**
     * Updates the quantity of a specific cart item.
     *
     * @param cartId the ID of the cart item
     * @param quantity the new quantity to set
     * @throws RuntimeException if the cart item is not found
     * @throws IllegalArgumentException if the quantity is less than 1
     */
    @Override
    public void updateCartQuantity(Long cartId, int quantity) {
        Cart cartItem = cartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));

        if (quantity < 1) {
            throw new IllegalArgumentException("Quantity must be at least 1");
        }

        cartItem.setQuantity(quantity);
        cartRepository.save(cartItem);
    }

    /**
     * Retrieves all cart items in the system.
     *
     * @return a list of all cart items
     */
    @Override
    public List<Cart> getAllCartItems() {
        return cartRepository.findAll();
    }

    /**
     * Retrieves a specific cart item by its ID.
     *
     * @param cartId the ID of the cart item
     * @return the cart item if found
     * @throws RuntimeException if the cart item is not found
     */
    @Override
    public Cart getCartItemById(Long cartId) {
        return cartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));
    }

    /**
     * Updates the quantity of multiple cart items in one request.
     *
     * @param cartUpdates a list of cart update requests
     * @throws RuntimeException if any cart item is not found
     * @throws IllegalArgumentException if the quantity is less than 1
     */
    @Override
    public void updateCartItems(List<CartUpdateRequest> cartUpdates) {
        for (CartUpdateRequest update : cartUpdates) {
            Cart cartItem = cartRepository.findById(update.getCartId())
                    .orElseThrow(() -> new RuntimeException("Cart item not found: " + update.getCartId()));

            if (update.getQuantity() < 1) {
                throw new IllegalArgumentException("Quantity must be at least 1 for cart item: " + update.getCartId());
            }

            cartItem.setQuantity(update.getQuantity());
            cartRepository.save(cartItem);
        }
    }
}
