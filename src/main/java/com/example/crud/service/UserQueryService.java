package com.example.crud.service;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.crud.model.User;
import com.example.crud.model.UserQuery;
import com.example.crud.repository.UserQueryRepository;

@Service
public class UserQueryService {

    private final UserQueryRepository userQueryRepository;

    @Autowired
    public UserQueryService(UserQueryRepository userQueryRepository) {
        this.userQueryRepository = userQueryRepository;
    }

    /**
     * Log a user query
     */
    public UserQuery logQuery(String queryType, String queryDetails, User user) {
        UserQuery query = new UserQuery(queryType, queryDetails, user);
        return userQueryRepository.save(query);
    }

    /**
     * Get all queries
     */
    public List<UserQuery> getAllQueries() {
        return userQueryRepository.findAll();
    }

    /**
     * Get queries by user
     */
    public List<UserQuery> getQueriesByUser(Long userId) {
        return userQueryRepository.findByUserId(userId);
    }

    /**
     * Get queries within a date range
     */
    public List<UserQuery> getQueriesByDateRange(LocalDateTime start, LocalDateTime end) {
        return userQueryRepository.findByTimestampBetween(start, end);
    }

    /**
     * Get count of queries by type
     */
    public Map<String, Long> getQueryCountByType() {
        List<Object[]> results = userQueryRepository.countQueriesByType();
        Map<String, Long> countByType = new LinkedHashMap<>();
        
        for (Object[] result : results) {
            String queryType = (String) result[0];
            Long count = ((Number) result[1]).longValue();
            countByType.put(queryType, count);
        }
        
        return countByType;
    }

    /**
     * Get query counts by day for a date range
     */
    public Map<String, Long> getQueryCountByDay(LocalDateTime startDate, LocalDateTime endDate) {
        List<Object[]> results = userQueryRepository.countQueriesByDay(startDate, endDate);
        Map<String, Long> countByDay = new LinkedHashMap<>();
        
        for (Object[] result : results) {
            // Assuming the date is returned as java.sql.Date or similar
            String date = result[0].toString();
            Long count = ((Number) result[1]).longValue();
            countByDay.put(date, count);
        }
        
        return countByDay;
    }

    /**
     * Get query counts by hour for the last 24 hours
     */
    public Map<Integer, Long> getQueryCountByHour() {
        LocalDateTime startDate = LocalDateTime.now().minusHours(24);
        List<Object[]> results = userQueryRepository.countQueriesByHour(startDate);
        Map<Integer, Long> countByHour = new LinkedHashMap<>();
        
        // Initialize all hours with 0 count
        for (int i = 0; i < 24; i++) {
            countByHour.put(i, 0L);
        }
        
        // Fill in actual counts
        for (Object[] result : results) {
            Integer hour = ((Number) result[0]).intValue();
            Long count = ((Number) result[1]).longValue();
            countByHour.put(hour, count);
        }
        
        return countByHour;
    }
}
