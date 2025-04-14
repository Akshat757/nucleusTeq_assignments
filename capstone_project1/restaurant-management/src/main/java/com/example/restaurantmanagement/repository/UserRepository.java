package com.example.restaurantmanagement.repository;

import com.example.restaurantmanagement.model.app_user;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<app_user, Long> {
    app_user findByEmail(String email);
}
