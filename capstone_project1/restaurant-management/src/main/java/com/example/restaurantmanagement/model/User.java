//package com.example.restaurantmanagement.model;
//
//import jakarta.persistence.*;
//import lombok.Data;
//
//@Entity
//@Table(name = "\"user\"")  // Quoted because "user" is a reserved keyword in PostgreSQL
//@Data
//public class User {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @Column(nullable = false, unique = true)
//    private String email;
//
//    @Column(nullable = false)
//    private String password;
//
//    @Column(nullable = false)
//    private String role; // "CUSTOMER" or "OWNER"
//
//    // Wallet balance
//    @Column(nullable = false, columnDefinition = "float default 1000")
//    private Double walletBalance = 1000.0;
//}
