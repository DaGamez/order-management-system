package com.example.crud.aspect;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.example.crud.model.User;
import com.example.crud.service.UserQueryService;
import com.example.crud.service.UserService;

@Aspect
@Component
public class UserQueryAspect {

    private static final Logger logger = LoggerFactory.getLogger(UserQueryAspect.class);
    
    private final UserQueryService userQueryService;
    private final UserService userService;

    @Autowired
    public UserQueryAspect(UserQueryService userQueryService, UserService userService) {
        this.userQueryService = userQueryService;
        this.userService = userService;
    }

    // Pointcut for product controllers
    @Pointcut("execution(* com.example.crud.controller.ProductController.*(..))")
    public void productControllerMethods() {}

    // Pointcut for order controllers
    @Pointcut("execution(* com.example.crud.controller.OrderController.*(..))")
    public void orderControllerMethods() {}
    
    // Pointcut for web controllers
    @Pointcut("execution(* com.example.crud.controller.WebController.*(..))")
    public void webControllerMethods() {}

    // Pointcut for order web controllers
    @Pointcut("execution(* com.example.crud.controller.OrderWebController.*(..))")
    public void orderWebControllerMethods() {}
    
    // Log product queries
    @AfterReturning("productControllerMethods()")
    public void logProductQuery(JoinPoint joinPoint) {
        try {
            String methodName = joinPoint.getSignature().getName();
            User user = getCurrentUser();
            if (user != null) {
                String queryType = "PRODUCT";
                String queryDetails = "Method: " + methodName + " | Args: " + formatArguments(joinPoint.getArgs());
                userQueryService.logQuery(queryType, queryDetails, user);
            }
        } catch (Exception e) {
            logger.error("Error logging product query", e);
        }
    }
    
    // Log order queries
    @AfterReturning("orderControllerMethods() || orderWebControllerMethods()")
    public void logOrderQuery(JoinPoint joinPoint) {
        try {
            String methodName = joinPoint.getSignature().getName();
            User user = getCurrentUser();
            if (user != null) {
                String queryType = "ORDER";
                String queryDetails = "Method: " + methodName + " | Args: " + formatArguments(joinPoint.getArgs());
                userQueryService.logQuery(queryType, queryDetails, user);
            }
        } catch (Exception e) {
            logger.error("Error logging order query", e);
        }
    }
    
    // Log web queries
    @AfterReturning("webControllerMethods()")
    public void logWebQuery(JoinPoint joinPoint) {
        try {
            String methodName = joinPoint.getSignature().getName();
            User user = getCurrentUser();
            if (user != null) {
                String queryType = "WEB";
                String queryDetails = "Method: " + methodName + " | Args: " + formatArguments(joinPoint.getArgs());
                userQueryService.logQuery(queryType, queryDetails, user);
            }
        } catch (Exception e) {
            logger.error("Error logging web query", e);
        }
    }
    
    // Helper method to get current user
    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !"anonymousUser".equals(authentication.getName())) {
            try {
                Optional<User> user = userService.getUserByUsername(authentication.getName());
                return user.orElse(null);
            } catch (EntityNotFoundException e) {
                logger.warn("Could not find user for query logging", e);
                return null;
            }
        }
        return null;
    }
    
    // Helper method to format arguments for query details
    private String formatArguments(Object[] args) {
        if (args == null || args.length == 0) {
            return "none";
        }
        
        StringBuilder sb = new StringBuilder();
        for (Object arg : args) {
            if (arg instanceof Authentication) {
                sb.append("Authentication, ");
            } else if (arg == null) {
                sb.append("null, ");
            } else {
                String argString = arg.toString();
                // Truncate long arguments
                if (argString.length() > 50) {
                    argString = argString.substring(0, 47) + "...";
                }
                sb.append(argString).append(", ");
            }
        }
        
        // Remove trailing comma and space if present
        if (sb.length() > 2) {
            sb.setLength(sb.length() - 2);
        }
        
        return sb.toString();
    }
}
