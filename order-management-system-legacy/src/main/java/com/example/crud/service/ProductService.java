package com.example.crud.service;

import com.example.crud.model.Product;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    List<Product> getAllProducts();
    
    Optional<Product> getProductById(Long id);
    
    Product createProduct(Product product);
    
    Product updateProduct(Long id, Product product);
    
    void deleteProduct(Long id);
    
    List<Product> findProductsByName(String name);
    
    List<Product> findProductsByPriceLessThan(Double price);
    
    List<Product> findProductsByPriceGreaterThan(Double price);
}
