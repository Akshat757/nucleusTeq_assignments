package com.example.restaurantmanagement.service.impl;

import com.example.restaurantmanagement.dto.CartUpdateRequest;
import com.example.restaurantmanagement.model.Cart;
import com.example.restaurantmanagement.repository.CartRepository;
import com.example.restaurantmanagement.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartRepository cartRepository;

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

    @Override
    public Cart updateCartItem(Cart cart) {
        return cartRepository.save(cart);
    }

    @Override
    public void deleteCartItem(Long id) {
        cartRepository.deleteById(id);
    }

    @Override
    public List<Cart> getCartItemsByCustomer(Long customerId) {
        return cartRepository.findByCustomer_Id(customerId);
    }

    @Override
    public Cart getCartItemByCustomerAndMenuItem(Long customerId, Long menuItemId) {
        return cartRepository.findByCustomer_IdAndMenuItem_Id(customerId, menuItemId);
    }

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

    // ✅ Get all cart items
    @Override
    public List<Cart> getAllCartItems() {
        return cartRepository.findAll();
    }

    // ✅ Get cart item by ID
    @Override
    public Cart getCartItemById(Long cartId) {
        return cartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));
    }

    // ✅ Update multiple cart items in one request
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
