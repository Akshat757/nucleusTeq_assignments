package com.example.restaurantmanagement.repository;

import com.example.restaurantmanagement.model.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {
    List<MenuItem> findByNameContainingIgnoreCase(String name);
    List<MenuItem> findByRestaurant_Id(Long restaurantId);
}
