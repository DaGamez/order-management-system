package com.example.crud.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.crud.model.UserQuery;

@Repository
public interface UserQueryRepository extends JpaRepository<UserQuery, Long> {
    
    // Find queries by user ID
    List<UserQuery> findByUserId(Long userId);
    
    // Find queries between dates
    List<UserQuery> findByTimestampBetween(LocalDateTime start, LocalDateTime end);
    
    // Count queries by type
    @Query("SELECT uq.queryType, COUNT(uq) FROM UserQuery uq GROUP BY uq.queryType")
    List<Object[]> countQueriesByType();
    
    // Count queries grouped by day
    @Query("SELECT FUNCTION('DATE', uq.timestamp) as date, COUNT(uq) as count FROM UserQuery uq " +
           "WHERE uq.timestamp BETWEEN :startDate AND :endDate " +
           "GROUP BY FUNCTION('DATE', uq.timestamp) ORDER BY date")
    List<Object[]> countQueriesByDay(LocalDateTime startDate, LocalDateTime endDate);
    
    // Count queries by hour for the last 24 hours
    @Query("SELECT FUNCTION('HOUR', uq.timestamp) as hour, COUNT(uq) as count FROM UserQuery uq " +
           "WHERE uq.timestamp >= :startDate " +
           "GROUP BY FUNCTION('HOUR', uq.timestamp) ORDER BY hour")
    List<Object[]> countQueriesByHour(LocalDateTime startDate);
}
