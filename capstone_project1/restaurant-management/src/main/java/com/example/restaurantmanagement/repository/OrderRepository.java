package com.example.restaurantmanagement.repository;

import com.example.restaurantmanagement.model.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Orders, Long> {
    List<Orders> findByCustomer_Id(Long customerId);
    List<Orders> findByRestaurantOwnerId(Long ownerId);
}
