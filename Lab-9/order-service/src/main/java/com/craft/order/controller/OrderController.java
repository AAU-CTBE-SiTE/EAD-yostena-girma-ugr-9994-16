package com.craft.order.controller;

import com.craft.order.dto.OrderWithCustomerResponse;
import com.craft.order.model.Order;
import com.craft.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public Order createOrder(@RequestBody Order order) {
        return orderService.saveOrder(order);
    }

    @GetMapping
    public List<Order> getAllOrders() {
        return orderService.getAllOrders();
    }

    @GetMapping("/{id}")
    public Order getOrderById(@PathVariable Long id) {
        return orderService.getOrderById(id);
    }

    // Which customer owns this order? RestClient → customer-service :8080
    @GetMapping("/{id}/with-customer")
    public OrderWithCustomerResponse getOrderWithCustomer(@PathVariable Long id) {
        return orderService.getOrderWithCustomer(id);
    }

    // What did this customer order? RestClient → customer-service :8080
    @GetMapping("/customer/{customerId}")
    public OrderWithCustomerResponse getOrderByCustomerId(@PathVariable String customerId) {
        return orderService.getOrderByCustomerId(customerId);
    }

    @PutMapping("/{id}")
    public Order updateOrder(@PathVariable Long id, @RequestBody Order order) {
        return orderService.updateOrder(id, order);
    }

    @DeleteMapping("/{id}")
    public void deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
    }
}
