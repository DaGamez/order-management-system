package com.example.crud.controller;

import com.example.crud.model.User;
import com.example.crud.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String getAllUsers(Model model) {
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "users/list";
    }

    @GetMapping("/new")
    public String showNewUserForm(Model model) {
        model.addAttribute("user", new User());
        return "users/form";
    }

    @PostMapping("/save")
    public String saveUser(@Valid @ModelAttribute("user") User user,
                          BindingResult result,
                          @RequestParam(value = "roles", required = false) Set<String> roles,
                          RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "users/form";
        }

        boolean isNew = (user.getId() == null);
        
        // Set the roles if provided
        if (roles != null && !roles.isEmpty()) {
            user.setRoles(roles);
        }

        try {
            if (isNew) {
                // Check if username already exists
                if (userService.usernameExists(user.getUsername())) {
                    result.rejectValue("username", "error.user", "Username already exists");
                    return "users/form";
                }
                userService.createUser(user);
                redirectAttributes.addFlashAttribute("message", "User created successfully!");
            } else {
                // Check if username already exists for another user
                User existingUser = userService.getUserByUsername(user.getUsername()).orElse(null);
                if (existingUser != null && !existingUser.getId().equals(user.getId())) {
                    result.rejectValue("username", "error.user", "Username already exists");
                    return "users/form";
                }
                userService.updateUser(user.getId(), user);
                redirectAttributes.addFlashAttribute("message", "User updated successfully!");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error: " + e.getMessage());
        }

        return "redirect:/users";
    }

    @GetMapping("/edit/{id}")
    public String showUpdateForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            User user = userService.getUserById(id)
                    .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
            model.addAttribute("user", user);
            return "users/form";
        } catch (EntityNotFoundException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/users";
        }
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            userService.deleteUser(id);
            redirectAttributes.addFlashAttribute("message", "User deleted successfully!");
        } catch (EntityNotFoundException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/users";
    }
}
