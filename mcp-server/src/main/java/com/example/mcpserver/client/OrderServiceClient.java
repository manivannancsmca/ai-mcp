package com.example.mcpserver.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;

@Component
public class OrderServiceClient {

    private final RestClient restClient;

    public OrderServiceClient(@Value("${order.service.base-url}") String baseUrl) {
        this.restClient = RestClient.builder().baseUrl(baseUrl).build();
    }

    public Map<String, Object> getOrderById(Long id) {
        return restClient.get()
                .uri("/api/orders/{id}", id)
                .retrieve()
                .body(new ParameterizedTypeReference<Map<String, Object>>() {});
    }

    public List<Map<String, Object>> getAllOrders() {
        return restClient.get()
                .uri("/api/orders")
                .retrieve()
                .body(new ParameterizedTypeReference<List<Map<String, Object>>>() {});
    }

    public List<Map<String, Object>> getOrdersByCustomer(String email) {
        return restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/orders/customer")
                        .queryParam("email", email)
                        .build())
                .retrieve()
                .body(new ParameterizedTypeReference<List<Map<String, Object>>>() {});
    }
}