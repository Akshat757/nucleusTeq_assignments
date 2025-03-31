package com.example.restaurantmanagement.repository;

import com.example.restaurantmanagement.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
    List<Restaurant> findByOwner_Id(Long ownerId);
    List<Restaurant> findByNameContainingIgnoreCase(String name);
}
