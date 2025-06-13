package com.example.crud.config;

import com.example.crud.model.Order;
import com.example.crud.model.Product;
import com.example.crud.model.User;
import com.example.crud.repository.OrderRepository;
import com.example.crud.repository.ProductRepository;
import com.example.crud.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.Date;
import java.util.Set;

@Configuration
public class DataInitializer {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);
    
    @Autowired
    private PasswordEncoder passwordEncoder;    @Bean
    public CommandLineRunner initData(ProductRepository productRepository, UserRepository userRepository, OrderRepository orderRepository) {
        return args -> {
            logger.info("Data initialization started...");
            
            // Initialize products if the database is empty
            if (productRepository.count() == 0) {
                logger.info("Populating database with sample products");
                
                productRepository.save(new Product("Laptop", "High-performance laptop with SSD", 1200.00));
                productRepository.save(new Product("Smartphone", "Latest model with advanced camera", 800.00));
                productRepository.save(new Product("Tablet", "10-inch display with long battery life", 350.00));
                productRepository.save(new Product("Monitor", "27-inch 4K display", 450.00));
                productRepository.save(new Product("Keyboard", "Mechanical gaming keyboard", 150.00));
                productRepository.save(new Product("Mouse", "Wireless ergonomic mouse", 50.00));
                productRepository.save(new Product("Headphones", "Noise-cancelling wireless headphones", 200.00));
                productRepository.save(new Product("Printer", "All-in-one printer with scanner", 300.00));
                productRepository.save(new Product("External Hard Drive", "1TB portable storage", 80.00));
                productRepository.save(new Product("USB Flash Drive", "64GB high-speed USB 3.0", 25.00));
                
                logger.info("Database initialized with {} products", productRepository.count());
            } else {
                logger.info("Database already contains {} products, skipping initialization", productRepository.count());
            }
            
            // Initialize users if the database is empty
            if (userRepository.count() == 0) {
                logger.info("Creating default users");
                
                // Create regular user
                User user = new User("user", passwordEncoder.encode("password"));
                user.setRoles(Set.of("USER"));
                userRepository.save(user);
                
                // Create admin user
                User admin = new User("admin", passwordEncoder.encode("admin"));
                admin.setRoles(Set.of("USER", "ADMIN"));
                userRepository.save(admin);
                
                // Create API user
                User apiUser = new User("api", passwordEncoder.encode("api123"));
                apiUser.setRoles(Set.of("API_USER"));
                userRepository.save(apiUser);
                
                logger.info("Default users created: {}", userRepository.count());            } else {
                logger.info("Users already exist in the database, skipping initialization");
            }
            
            // Initialize sample orders if there are none
            if (orderRepository.count() == 0 && productRepository.count() > 0 && userRepository.count() > 0) {
                logger.info("Creating sample orders");
                
                // Get some existing users and products to create orders
                User regularUser = userRepository.findByUsername("user").orElse(null);
                User adminUser = userRepository.findByUsername("admin").orElse(null);
                
                if (regularUser != null && adminUser != null) {
                    Product laptop = productRepository.findById(1L).orElse(null);
                    Product smartphone = productRepository.findById(2L).orElse(null);
                    Product tablet = productRepository.findById(3L).orElse(null);
                    
                    if (laptop != null && smartphone != null && tablet != null) {                        // Create orders for regular user
                        Order order1 = new Order(LocalDate.now(), regularUser, laptop, 1, "Need urgent delivery");
                        orderRepository.save(order1);
                        
                        Order order2 = new Order(LocalDate.now(), regularUser, smartphone, 2, "Gift package please");
                        orderRepository.save(order2);
                        
                        // Create an order for admin user
                        Order order3 = new Order(LocalDate.now(), adminUser, tablet, 1, "For testing purposes");
                        orderRepository.save(order3);
                        
                        logger.info("Sample orders created: {}", orderRepository.count());
                    }
                }
            } else {
                logger.info("Orders already exist or prerequisites missing, skipping order initialization");
            }
            
            logger.info("Data initialization completed");
        };
    }
}
