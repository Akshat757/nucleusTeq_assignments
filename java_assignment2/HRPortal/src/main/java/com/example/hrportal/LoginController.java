package com.example.hrportal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import jakarta.servlet.http.HttpSession;

@Controller
public class LoginController {
    @Autowired // Automatically injects HRUserRepository
    private HRUserRepository hrUserRepo;

    // Show login page
    @GetMapping("/")
    public String loginPage() {
        return "login"; // Refers to login.html
    }

    // Handle login form submission
    @PostMapping("/login")
    public String login(
            @RequestParam String email,
            @RequestParam String password,
            HttpSession session, // Store logged-in user in session
            Model model // Pass data to HTML
    ) {
        HRUser user = hrUserRepo.findById(email).orElse(null);
        if (user != null && user.getPassword().equals(password)) {
            session.setAttribute("hrUser", email); // Mark user as logged in
            return "redirect:/employees"; // Redirect to employee list
        } else {
            model.addAttribute("error", "Invalid email or password");
            return "login"; // Show error on login page
        }
    }
}