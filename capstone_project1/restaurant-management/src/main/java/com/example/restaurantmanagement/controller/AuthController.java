package com.example.restaurantmanagement.controller;

//import com.example.restaurantmanagement.model.User;
import com.example.restaurantmanagement.model.app_user;
import com.example.restaurantmanagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public app_user register(@RequestBody app_user user) {
        return userService.registerUser(user);
    }

    @PostMapping("/login")
    public app_user login(@RequestBody app_user user) {
        return userService.loginUser(user.getEmail(), user.getPassword());
    }

}
