package com.example.crud.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class LogDirectoryConfig {
    
    private static final Logger logger = LoggerFactory.getLogger(LogDirectoryConfig.class);
    
    @Bean
    @Order(1) // Ensure this runs before data initialization
    public CommandLineRunner createLogDirectories() {
        return args -> {
            // Create logs directory if it doesn't exist
            Path logsDir = Paths.get("logs");
            if (!Files.exists(logsDir)) {
                try {
                    Files.createDirectories(logsDir);
                    logger.info("Created logs directory: {}", logsDir.toAbsolutePath());
                } catch (IOException e) {
                    logger.error("Failed to create logs directory", e);
                }
            }
            
            // Create archived logs directory if it doesn't exist
            Path archivedLogsDir = Paths.get("logs/archived");
            if (!Files.exists(archivedLogsDir)) {
                try {
                    Files.createDirectories(archivedLogsDir);
                    logger.info("Created archived logs directory: {}", archivedLogsDir.toAbsolutePath());
                } catch (IOException e) {
                    logger.error("Failed to create archived logs directory", e);
                }
            }
        };
    }
}
