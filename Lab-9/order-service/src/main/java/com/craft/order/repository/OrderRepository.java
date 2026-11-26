package com.craft.order.repository;

import com.craft.order.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    Optional<Order> findByCustomerId(String customerId);

    boolean existsByCustomerId(String customerId);
}
