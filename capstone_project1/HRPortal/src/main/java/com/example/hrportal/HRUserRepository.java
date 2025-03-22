package com.example.hrportal;

import org.springframework.data.jpa.repository.JpaRepository;

public interface HRUserRepository extends JpaRepository<HRUser, String> {
    // "String" because the primary key (email) is a String
}
