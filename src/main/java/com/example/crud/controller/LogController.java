package com.example.crud.controller;

import com.example.crud.service.LogService;
import com.example.crud.service.LogService.LogType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/admin/logs")
public class LogController {

    private static final Logger logger = LoggerFactory.getLogger(LogController.class);
    private final LogService logService;

    @Autowired
    public LogController(LogService logService) {
        this.logService = logService;
    }

    /**
     * View the current logs (defaults to database logs)
     */
    @GetMapping
    public String viewCurrentLogs(
            @RequestParam(required = false, defaultValue = "DATABASE") LogType logType,
            @RequestParam(required = false, defaultValue = "500") Integer lines,
            Model model) {
        logger.debug("Viewing current {} logs with {} lines", logType, lines);
        List<String> logs = logService.getCurrentLogs(logType, lines);
        List<LocalDate> availableDates = logService.getAvailableLogDates(logType);
        
        model.addAttribute("logType", logType);
        model.addAttribute("viewType", "current");
        model.addAttribute("logs", logs);
        model.addAttribute("lines", lines);
        model.addAttribute("availableDates", availableDates);
        model.addAttribute("logTypes", LogType.values());
        
        return "admin/logs";
    }

    /**
     * View archived logs by date
     */
    @GetMapping("/archived")
    public String viewArchivedLogs(
            @RequestParam(required = false, defaultValue = "DATABASE") LogType logType,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(required = false, defaultValue = "500") Integer lines,
            Model model) {
        logger.debug("Viewing archived {} logs for date {} with {} lines", logType, date, lines);
        List<String> logs = logService.getArchivedLogs(logType, date, lines);
        List<LocalDate> availableDates = logService.getAvailableLogDates(logType);
        
        model.addAttribute("logType", logType);
        model.addAttribute("viewType", "archived");
        model.addAttribute("logs", logs);
        model.addAttribute("date", date);
        model.addAttribute("lines", lines);
        model.addAttribute("availableDates", availableDates);
        model.addAttribute("logTypes", LogType.values());
        
        return "admin/logs";
    }
}
