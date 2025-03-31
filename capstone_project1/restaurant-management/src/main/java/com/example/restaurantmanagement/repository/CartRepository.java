package com.example.restaurantmanagement.repository;

import com.example.restaurantmanagement.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    List<Cart> findByCustomer_Id(Long customerId);
    Cart findByCustomer_IdAndMenuItem_Id(Long customerId, Long menuItemId);
}
