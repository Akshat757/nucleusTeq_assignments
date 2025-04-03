package com.example.restaurantmanagement.service;

import com.example.restaurantmanagement.dto.CartUpdateRequest;
import com.example.restaurantmanagement.model.Cart;
import java.util.List;

public interface CartService {
    Cart addCartItem(Cart cart);
    Cart updateCartItem(Cart cart);
    void deleteCartItem(Long id);
    List<Cart> getCartItemsByCustomer(Long customerId);
    Cart getCartItemByCustomerAndMenuItem(Long customerId, Long menuItemId);
    void updateCartQuantity(Long cartId, int quantity);

    // New methods
    List<Cart> getAllCartItems();
    Cart getCartItemById(Long cartId);
    void updateCartItems(List<CartUpdateRequest> cartUpdates);
}
