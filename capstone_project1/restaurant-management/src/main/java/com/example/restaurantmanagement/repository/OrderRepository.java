package com.example.restaurantmanagement.repository;

import com.example.restaurantmanagement.model.Orders;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Orders, Long> {
    List<Orders> findByCustomer_Id(Long customerId);

    @Query("SELECT r.id, r.name, COUNT(o.id) as orderCount " +
            "FROM Restaurant r JOIN Orders o ON r.id = o.restaurant.id " +
            "WHERE r.owner.id = :ownerId " + // Filter by owner
            "GROUP BY r.id, r.name " +
            "ORDER BY orderCount DESC")
    List<Object[]> findTopRestaurantsByOwner(@Param("ownerId") Long ownerId, Pageable pageable);


    @Query("SELECT o FROM Orders o WHERE o.restaurant.owner.id = :ownerId")
    List<Orders> findByRestaurantOwnerId(Long ownerId);
}
