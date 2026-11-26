package com.craft.order.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class CustomerRestClientConfig {

    @Value("${customer.service.url}")
    private String customerServiceUrl;

    @Bean
    public RestClient customerServiceRestClient() {
        return RestClient.builder()
                .baseUrl(customerServiceUrl)
                .build();
    }
}
