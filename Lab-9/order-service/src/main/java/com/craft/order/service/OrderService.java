package com.craft.order.service;

import com.craft.order.dto.CustomerDto;
import com.craft.order.dto.OrderWithCustomerResponse;
import com.craft.order.model.Order;
import com.craft.order.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final RestClient customerServiceRestClient;

    @Autowired
    public OrderService(OrderRepository orderRepository, RestClient customerServiceRestClient) {
        this.orderRepository = orderRepository;
        this.customerServiceRestClient = customerServiceRestClient;
    }

    public Order saveOrder(Order order) {
        if (orderRepository.existsByCustomerId(order.getCustomerId())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "This customer already has an order (one-to-one)"
            );
        }
        fetchCustomer(order.getCustomerId());
        return orderRepository.save(order);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found"));
    }

    public OrderWithCustomerResponse getOrderWithCustomer(Long id) {
        Order order = getOrderById(id);
        return new OrderWithCustomerResponse(order, fetchCustomer(order.getCustomerId()));
    }

    public OrderWithCustomerResponse getOrderByCustomerId(String customerId) {
        Order order = orderRepository.findByCustomerId(customerId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found for customer"));
        return new OrderWithCustomerResponse(order, fetchCustomer(customerId));
    }

    public Order updateOrder(Long id, Order order) {
        Order existingOrder = getOrderById(id);

        if (!existingOrder.getCustomerId().equals(order.getCustomerId())
                && orderRepository.existsByCustomerId(order.getCustomerId())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "This customer already has an order (one-to-one)"
            );
        }

        fetchCustomer(order.getCustomerId());
        existingOrder.setProduct(order.getProduct());
        existingOrder.setQuantity(order.getQuantity());
        existingOrder.setPrice(order.getPrice());
        existingOrder.setCustomerId(order.getCustomerId());
        return orderRepository.save(existingOrder);
    }

    public void deleteOrder(Long id) {
        if (!orderRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found");
        }
        orderRepository.deleteById(id);
    }

    private CustomerDto fetchCustomer(String customerId) {
        try {
            CustomerDto customer = customerServiceRestClient.get()
                    .uri("/api/customers/{id}", customerId)
                    .retrieve()
                    .body(CustomerDto.class);

            if (customer == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found");
            }
            return customer;
        } catch (RestClientResponseException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found in customer-service");
        }
    }
}
