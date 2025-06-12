package com.example.crud.config;

import com.example.crud.model.Product;
import com.example.crud.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Configuration
public class DataInitializer {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    @Bean
    public CommandLineRunner initData(ProductRepository productRepository) {
        return args -> {
            logger.info("Data initialization started...");
            
            // Only initialize if the database is empty
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
            
            logger.info("Data initialization completed");
        };
    }
}
