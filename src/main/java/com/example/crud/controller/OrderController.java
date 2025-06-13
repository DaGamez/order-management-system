package com.example.crud.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.crud.model.Order;
import com.example.crud.model.User;
import com.example.crud.service.OrderService;
import com.example.crud.service.UserService;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;
    private final UserService userService;

    @Autowired
    public OrderController(OrderService orderService, UserService userService) {
        this.orderService = orderService;
        this.userService = userService;
    }

    // Get all orders (admin only)
    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders() {
        List<Order> orders = orderService.getAllOrders();
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    // Get current user's orders    @GetMapping("/my-orders")
    public ResponseEntity<List<Order>> getMyOrders(Authentication authentication) {
        User currentUser = userService.getUserByUsername(authentication.getName())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        List<Order> orders = orderService.getOrdersByUser(currentUser);
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    // Get an order by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderById(@PathVariable Long id, Authentication authentication) {
        return orderService.getOrderById(id)                .map(order -> {
                    // Check if the order belongs to current user or user is admin
                    User currentUser = userService.getUserByUsername(authentication.getName())
                            .orElseThrow(() -> new EntityNotFoundException("User not found"));
                    
                    boolean isAdmin = authentication.getAuthorities().stream()
                            .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
                    
                    if (isAdmin || order.getUser().getId().equals(currentUser.getId())) {
                        return new ResponseEntity<>(order, HttpStatus.OK);
                    } else {
                        Map<String, String> error = new HashMap<>();
                        error.put("message", "You do not have permission to view this order");
                        return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
                    }
                })
                .orElseGet(() -> {
                    Map<String, String> error = new HashMap<>();
                    error.put("message", "Order not found with id: " + id);
                    return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
                });
    }    // Create a new order
    @PostMapping
    public ResponseEntity<Order> createOrder(@Valid @RequestBody Order order, Authentication authentication) {
        User currentUser = userService.getUserByUsername(authentication.getName())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        order.setUser(currentUser);
        Order newOrder = orderService.createOrder(order);
        return new ResponseEntity<>(newOrder, HttpStatus.CREATED);
    }

    // Update an order
    @PutMapping("/{id}")
    public ResponseEntity<?> updateOrder(@PathVariable Long id, @Valid @RequestBody Order order, Authentication authentication) {        return orderService.getOrderById(id)
                .map(existingOrder -> {
                    User currentUser = userService.getUserByUsername(authentication.getName())
                            .orElseThrow(() -> new EntityNotFoundException("User not found"));
                    
                    boolean isAdmin = authentication.getAuthorities().stream()
                            .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
                    
                    if (isAdmin || existingOrder.getUser().getId().equals(currentUser.getId())) {
                        order.setId(id);
                        order.setUser(existingOrder.getUser());
                        Order updatedOrder = orderService.updateOrder(order);
                        return new ResponseEntity<>(updatedOrder, HttpStatus.OK);
                    } else {
                        Map<String, String> error = new HashMap<>();
                        error.put("message", "You do not have permission to update this order");
                        return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
                    }
                })
                .orElseGet(() -> {
                    Map<String, String> error = new HashMap<>();
                    error.put("message", "Order not found with id: " + id);
                    return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
                });
    }

    // Delete an order
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrder(@PathVariable Long id, Authentication authentication) {        return orderService.getOrderById(id)
                .map(order -> {
                    User currentUser = userService.getUserByUsername(authentication.getName())
                            .orElseThrow(() -> new EntityNotFoundException("User not found"));
                    
                    boolean isAdmin = authentication.getAuthorities().stream()
                            .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
                    
                    if (isAdmin || order.getUser().getId().equals(currentUser.getId())) {
                        orderService.deleteOrder(id);
                        Map<String, String> response = new HashMap<>();
                        response.put("message", "Order deleted successfully");
                        return new ResponseEntity<>(response, HttpStatus.OK);
                    } else {
                        Map<String, String> error = new HashMap<>();
                        error.put("message", "You do not have permission to delete this order");
                        return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
                    }
                })
                .orElseGet(() -> {
                    Map<String, String> error = new HashMap<>();
                    error.put("message", "Order not found with id: " + id);
                    return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
                });
    }
}
