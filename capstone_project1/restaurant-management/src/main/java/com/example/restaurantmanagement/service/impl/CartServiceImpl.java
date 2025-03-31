package com.example.restaurantmanagement.service.impl;

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
        // If the item is already in the cart, update the quantity.
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
}
