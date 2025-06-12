package com.example.crud.repository;

import com.example.crud.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    // Find products by name containing the given string (case insensitive)
    List<Product> findByNameContainingIgnoreCase(String name);
    
    // Find products with price less than the given value
    List<Product> findByPriceLessThan(Double price);
    
    // Find products with price greater than the given value
    List<Product> findByPriceGreaterThan(Double price);
}
