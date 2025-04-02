package com.example.restaurantmanagement.controller;

//import com.example.restaurantmanagement.model.User;
import com.example.restaurantmanagement.model.app_user;
import com.example.restaurantmanagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/{id}")
    public app_user getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @GetMapping("/by-email")
    public app_user getUserByEmail(@RequestParam String email) {
        return userService.getUserByEmail(email);
    }
}
