package com.example.crud.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.crud.service.UserQueryService;

@Controller
@RequestMapping("/admin/dashboard")
public class DashboardController {

    private final UserQueryService userQueryService;

    @Autowired
    public DashboardController(UserQueryService userQueryService) {
        this.userQueryService = userQueryService;
    }

    @GetMapping
    public String showDashboard(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            Model model) {
        
        // Default to last 7 days if dates not provided
        if (startDate == null) {
            startDate = LocalDate.now().minusDays(7);
        }
        if (endDate == null) {
            endDate = LocalDate.now();
        }
        
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);
        
        // Get query statistics
        Map<String, Long> queriesByDay = userQueryService.getQueryCountByDay(startDateTime, endDateTime);
        Map<String, Long> queriesByType = userQueryService.getQueryCountByType();
        Map<Integer, Long> queriesByHour = userQueryService.getQueryCountByHour();
        
        // Add data to model
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        model.addAttribute("queriesByDay", queriesByDay);
        model.addAttribute("queriesByType", queriesByType);
        model.addAttribute("queriesByHour", queriesByHour);
        
        return "admin/dashboard";
    }
}
