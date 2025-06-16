package com.example.crud.controller;

import java.util.Arrays;
import java.util.Optional;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.crud.model.Product;
import com.example.crud.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(controllers = ProductController.class)
@AutoConfigureMockMvc(addFilters = false) // Disable Spring Security filters for testing
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void getAllProducts_ShouldReturnAllProducts() throws Exception {
        // Given
        Product product1 = new Product("Product 1", "Description 1", 10.0);
        Product product2 = new Product("Product 2", "Description 2", 20.0);
        when(productService.getAllProducts()).thenReturn(Arrays.asList(product1, product2));

        // When & Then
        mockMvc.perform(get("/api/products")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is("Product 1")))
                .andExpect(jsonPath("$[1].name", is("Product 2")));

        verify(productService).getAllProducts();
    }

    @Test
    public void getProductById_WhenProductExists_ShouldReturnProduct() throws Exception {
        // Given
        Product product = new Product("Product 1", "Description 1", 10.0);
        when(productService.getProductById(1L)).thenReturn(Optional.of(product));

        // When & Then
        mockMvc.perform(get("/api/products/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Product 1")));

        verify(productService).getProductById(1L);
    }

    @Test
    public void getProductById_WhenProductDoesNotExist_ShouldReturnNotFound() throws Exception {
        // Given
        when(productService.getProductById(1L)).thenReturn(Optional.empty());

        // When & Then
        mockMvc.perform(get("/api/products/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", containsString("not found")));

        verify(productService).getProductById(1L);
    }

    @Test
    public void createProduct_WithValidData_ShouldCreateAndReturnProduct() throws Exception {
        // Given
        Product product = new Product("New Product", "New Description", 15.0);
        when(productService.createProduct(any(Product.class))).thenReturn(product);

        // When & Then
        mockMvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(product)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is("New Product")));

        verify(productService).createProduct(any(Product.class));
    }

    @Test
    public void updateProduct_WhenProductExists_ShouldUpdateAndReturnProduct() throws Exception {
        // Given
        Product product = new Product("Updated Product", "Updated Description", 25.0);
        when(productService.updateProduct(anyLong(), any(Product.class))).thenReturn(product);

        // When & Then
        mockMvc.perform(put("/api/products/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(product)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Updated Product")));

        verify(productService).updateProduct(anyLong(), any(Product.class));
    }

    @Test
    public void deleteProduct_WhenProductExists_ShouldDeleteAndReturnMessage() throws Exception {
        // Given
        doNothing().when(productService).deleteProduct(1L);

        // When & Then
        mockMvc.perform(delete("/api/products/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", containsString("successfully deleted")));

        verify(productService).deleteProduct(1L);
    }
}
