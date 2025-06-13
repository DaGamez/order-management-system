package com.example.crud.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {

    /**
     * Redirect admin root to logs page
     */
    @GetMapping
    public String adminHome() {
        return "redirect:/admin/logs";
    }
}
