package com.example.hrportal;

import jakarta.persistence.*;
import jakarta.persistence.Entity;

@Entity // Tells Spring this is a database table
public class Employee {
    @Id // Marks this field as the primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-generate ID
    private Long id;
    private String name;
    private String department;
    private String email;
    private double salary;

    // Getters and Setters (Required for JPA)
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public double getSalary() { return salary; }
    public void setSalary(double salary) { this.salary = salary; }
}
