package com.example.crud.service;

import com.example.crud.model.Order;
import com.example.crud.model.User;

import java.util.List;
import java.util.Optional;

public interface OrderService {
    List<Order> getAllOrders();
    List<Order> getOrdersByUser(User user);
    Optional<Order> getOrderById(Long id);
    Order createOrder(Order order);
    Order updateOrder(Order order);
    void deleteOrder(Long id);
}
