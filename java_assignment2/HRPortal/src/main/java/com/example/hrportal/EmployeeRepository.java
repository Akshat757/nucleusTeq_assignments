package com.example.hrportal;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    // Add custom query for sorted results
    List<Employee> findAllByOrderByIdAsc();
}