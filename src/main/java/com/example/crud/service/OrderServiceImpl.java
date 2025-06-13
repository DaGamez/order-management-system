package com.example.crud.service;

import com.example.crud.model.Order;
import com.example.crud.model.User;
import com.example.crud.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final LogService logService;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, LogService logService) {
        this.orderRepository = orderRepository;
        this.logService = logService;
    }

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public List<Order> getOrdersByUser(User user) {
        return orderRepository.findByUserOrderByCreatedAtDesc(user);
    }

    @Override
    public Optional<Order> getOrderById(Long id) {
        return orderRepository.findById(id);
    }

    @Override
    public Order createOrder(Order order) {
        Order savedOrder = orderRepository.save(order);
        logService.logAction(LogService.LogType.INFO, "Order created with ID: " + savedOrder.getId());
        return savedOrder;
    }

    @Override
    public Order updateOrder(Order order) {
        if (!orderRepository.existsById(order.getId())) {
            throw new EntityNotFoundException("Order not found with id: " + order.getId());        }
        Order updatedOrder = orderRepository.save(order);
        logService.logAction(LogService.LogType.INFO, "Order updated with ID: " + updatedOrder.getId());
        return updatedOrder;
    }

    @Override
    public void deleteOrder(Long id) {
        if (!orderRepository.existsById(id)) {
            throw new EntityNotFoundException("Order not found with id: " + id);
        }
        orderRepository.deleteById(id);
        logService.logAction(LogService.LogType.INFO, "Order deleted with ID: " + id);
    }
}
