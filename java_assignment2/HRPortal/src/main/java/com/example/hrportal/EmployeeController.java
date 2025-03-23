package com.example.hrportal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/employees")
public class EmployeeController {
    @Autowired
    private EmployeeRepository employeeRepo;

    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    @GetMapping
    public String listEmployees(Model model, HttpSession session) {
        if (session.getAttribute("hrUser") == null) return "redirect:/";

        // Get employees sorted by ID in ascending order
        model.addAttribute("employees", employeeRepo.findAllByOrderByIdAsc());
        return "employee-list";
    }

    // Show form to add a new employee
    @GetMapping("/new")
    public String showAddForm(Model model, HttpSession session) {
        if (session.getAttribute("hrUser") == null) return "redirect:/";
        model.addAttribute("employee", new Employee());
        return "employee-form"; // Show form
    }

    // Save or update an employee
    @PostMapping("/save")
    public String saveEmployee(@ModelAttribute Employee employee) {
        employeeRepo.save(employee); // Save to database
        return "redirect:/employees"; // Redirect to list
    }

    // Edit an employee
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model, HttpSession session) {
        if (session.getAttribute("hrUser") == null) return "redirect:/";
        Employee employee = employeeRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid employee ID"));
        model.addAttribute("employee", employee);
        return "employee-form"; // Pre-filled form
    }

    // Delete an employee
    @GetMapping("/delete/{id}")
    public String deleteEmployee(@PathVariable Long id, HttpSession session) {
        if (session.getAttribute("hrUser") == null) return "redirect:/";
        employeeRepo.deleteById(id);
        return "redirect:/employees";
    }
}