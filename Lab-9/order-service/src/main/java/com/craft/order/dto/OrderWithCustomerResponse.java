package com.craft.order.dto;

import com.craft.order.model.Order;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrderWithCustomerResponse {

    private Order order;
    private CustomerDto customer;
}
