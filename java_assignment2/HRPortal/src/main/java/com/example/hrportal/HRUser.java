package com.example.hrportal;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class HRUser {
    @Id // Email is the primary key
    private String email;
    private String password;

    // Getters and Setters
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
