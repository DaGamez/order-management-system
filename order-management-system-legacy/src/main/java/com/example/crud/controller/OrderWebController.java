package com.example.crud.controller;

import java.util.List;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.crud.model.Order;
import com.example.crud.model.Product;
import com.example.crud.model.User;
import com.example.crud.service.OrderService;
import com.example.crud.service.ProductService;
import com.example.crud.service.UserService;

@Controller
@RequestMapping("/orders")
public class OrderWebController {

    private final OrderService orderService;
    private final ProductService productService;
    private final UserService userService;

    @Autowired
    public OrderWebController(OrderService orderService, ProductService productService, UserService userService) {
        this.orderService = orderService;
        this.productService = productService;
        this.userService = userService;
    }    @GetMapping
    public String getAllOrders(Model model, Authentication authentication) {
        User currentUser = userService.getUserByUsername(authentication.getName())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        
        List<Order> orders;
        if (isAdmin) {
            orders = orderService.getAllOrders();
            model.addAttribute("isAdmin", true);
        } else {
            orders = orderService.getOrdersByUser(currentUser);
            model.addAttribute("isAdmin", false);
        }
        
        model.addAttribute("orders", orders);
        return "orders/list";
    }

    @GetMapping("/new")
    public String showNewOrderForm(Model model) {
        model.addAttribute("order", new Order());
        List<Product> products = productService.getAllProducts();
        model.addAttribute("products", products);
        return "orders/form";
    }

    @PostMapping("/save")
    public String saveOrder(@Valid @ModelAttribute("order") Order order,
                           BindingResult result,
                           Authentication authentication,
                           Model model,
                           RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            List<Product> products = productService.getAllProducts();
            model.addAttribute("products", products);
            return "orders/form";        }

        User currentUser = userService.getUserByUsername(authentication.getName())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        
        order.setUser(currentUser);
        
        if (order.getId() == null) {
            orderService.createOrder(order);
            redirectAttributes.addFlashAttribute("successMessage", "Order created successfully!");
        } else {
            orderService.updateOrder(order);
            redirectAttributes.addFlashAttribute("successMessage", "Order updated successfully!");
        }
        
        return "redirect:/orders";
    }    @GetMapping("/edit/{id}")
    public String showEditOrderForm(@PathVariable Long id, Model model, Authentication authentication) {
        Order order = orderService.getOrderById(id)
                .orElseThrow(() -> new EntityNotFoundException("Order not found with id: " + id));
        
        User currentUser = userService.getUserByUsername(authentication.getName())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        
        if (!isAdmin && !order.getUser().getId().equals(currentUser.getId())) {
            throw new SecurityException("You do not have permission to edit this order");
        }
        
        model.addAttribute("order", order);
        List<Product> products = productService.getAllProducts();
        model.addAttribute("products", products);
        return "orders/form";
    }

    @GetMapping("/delete/{id}")    public String deleteOrder(@PathVariable Long id, Authentication authentication, RedirectAttributes redirectAttributes) {
        Order order = orderService.getOrderById(id)
                .orElseThrow(() -> new EntityNotFoundException("Order not found with id: " + id));
        
        User currentUser = userService.getUserByUsername(authentication.getName())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        
        if (!isAdmin && !order.getUser().getId().equals(currentUser.getId())) {
            throw new SecurityException("You do not have permission to delete this order");
        }
        
        orderService.deleteOrder(id);
        redirectAttributes.addFlashAttribute("successMessage", "Order deleted successfully!");
        return "redirect:/orders";
    }
}
