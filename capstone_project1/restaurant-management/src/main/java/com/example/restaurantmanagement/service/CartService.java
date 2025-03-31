package com.example.restaurantmanagement.service;

import com.example.restaurantmanagement.model.Cart;
import java.util.List;

public interface CartService {
    Cart addCartItem(Cart cart);
    Cart updateCartItem(Cart cart);
    void deleteCartItem(Long id);
    List<Cart> getCartItemsByCustomer(Long customerId);
    Cart getCartItemByCustomerAndMenuItem(Long customerId, Long menuItemId);
}
