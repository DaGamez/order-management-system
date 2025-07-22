package com.example.crud.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class LogService {

    private static final Logger logger = LoggerFactory.getLogger(LogService.class);
    private static final String APPLICATION_LOG_PATH = "logs/application.log";
    private static final String DATABASE_LOG_PATH = "logs/database.log";
    private static final String ARCHIVED_LOGS_DIR = "logs/archived/";    public enum LogType {
        APPLICATION,
        DATABASE,
        INFO,
        WARNING,
        ERROR
    }

    /**
     * Get the content of the current log file
     * @param logType The type of log to retrieve (application or database)
     * @param lines Number of lines to retrieve (negative value means all lines)
     * @return List of log lines
     */
    public List<String> getCurrentLogs(LogType logType, int lines) {
        Path logFile = Paths.get(logType == LogType.DATABASE ? DATABASE_LOG_PATH : APPLICATION_LOG_PATH);
        return readLogFile(logFile, lines);
    }

    /**
     * Get the content of an archived log file
     * @param logType The type of log to retrieve (application or database)
     * @param date The date of the archived log in yyyy-MM-dd format
     * @param lines Number of lines to retrieve (negative value means all lines)
     * @return List of log lines
     */
    public List<String> getArchivedLogs(LogType logType, LocalDate date, int lines) {
        String filePrefix = logType == LogType.DATABASE ? "database" : "application";
        String fileName = String.format("%s.%s.log", filePrefix, 
                date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        Path logFile = Paths.get(ARCHIVED_LOGS_DIR, fileName);
        return readLogFile(logFile, lines);
    }

    /**
     * Get a list of available archived log files
     * @param logType The type of log to list (application or database)
     * @return List of dates for which archived logs exist
     */
    public List<LocalDate> getAvailableLogDates(LogType logType) {
        Path archiveDir = Paths.get(ARCHIVED_LOGS_DIR);
        if (!Files.exists(archiveDir)) {
            logger.warn("Archive directory does not exist: {}", ARCHIVED_LOGS_DIR);
            return Collections.emptyList();
        }

        try (Stream<Path> paths = Files.list(archiveDir)) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String filePrefix = logType == LogType.DATABASE ? "database" : "application";
            String pattern = filePrefix + "\\.\\d{4}-\\d{2}-\\d{2}\\.log";
            
            return paths
                .filter(Files::isRegularFile)
                .map(p -> p.getFileName().toString())
                .filter(name -> name.matches(pattern))
                .map(name -> {
                    // Extract yyyy-MM-dd portion (position depends on prefix length)
                    int prefixLength = filePrefix.length() + 1; // +1 for the dot
                    String dateStr = name.substring(prefixLength, prefixLength + 10);
                    return LocalDate.parse(dateStr, formatter);
                })
                .sorted(Collections.reverseOrder())
                .collect(Collectors.toList());
        } catch (IOException e) {
            logger.error("Error reading archived logs directory", e);
            return Collections.emptyList();
        }
    }

    /**
     * Read specific lines from a log file
     * @param logFile Path to the log file
     * @param lines Number of lines to read (negative value means all lines)
     * @return List of log lines
     */
    private List<String> readLogFile(Path logFile, int lines) {
        if (!Files.exists(logFile)) {
            logger.warn("Log file does not exist: {}", logFile);
            return Collections.emptyList();
        }

        try {
            List<String> allLines = Files.readAllLines(logFile);
            
            if (lines < 0 || lines >= allLines.size()) {
                return allLines;
            } else {
                return allLines.subList(Math.max(0, allLines.size() - lines), allLines.size());
            }
        } catch (IOException e) {
            logger.error("Error reading log file: {}", logFile, e);
            return Collections.emptyList();
        }
    }

    /**
     * Log an action with the specified log type and message
     * @param logType The type of log entry (INFO, WARNING, ERROR)
     * @param message The message to log
     */
    public void logAction(LogType logType, String message) {
        switch (logType) {
            case INFO:
                logger.info(message);
                break;
            case WARNING:
                logger.warn(message);
                break;
            case ERROR:
                logger.error(message);
                break;
            default:
                // For APPLICATION and DATABASE types, default to info level
                logger.info(message);
                break;
        }
        
        // Also write to application log file for tracking
        try {
            Path logFile = Paths.get(APPLICATION_LOG_PATH);
            // Ensure parent directories exist
            if (!Files.exists(logFile.getParent())) {
                Files.createDirectories(logFile.getParent());
            }
            // Append the log entry
            String logEntry = String.format("[%s] %s: %s%n", 
                    java.time.LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                    logType.name(),
                    message);
            Files.write(logFile, logEntry.getBytes(), 
                    java.nio.file.StandardOpenOption.APPEND, 
                    java.nio.file.StandardOpenOption.CREATE);
        } catch (IOException e) {
            logger.error("Failed to write to log file", e);
        }
    }
}
