package com.example.mcpserver.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import java.util.List;
import java.util.Map;

@Component
public class ProductServiceClient {
    private final RestClient restClient;

    public ProductServiceClient(@Value("${product.service.base-url}") String baseUrl) {
        this.restClient = RestClient.builder().baseUrl(baseUrl).build();
    }

    public Map getProductById(Long id) {
        return restClient.get()
                .uri("/api/products/{id}", id)
                .retrieve()
                .body(Map.class);
    }

    public List getAllProducts() {
        return restClient.get()
                .uri("/api/products")
                .retrieve()
                .body(List.class);
    }

    public List searchByName(String name) {
        return restClient.get()
                .uri(uriBuilder -> uriBuilder.path("/api/products/search")
                        .queryParam("name", name).build())
                .retrieve()
                .body(List.class);
    }

    public Boolean checkAvailability(Long id) {
        return restClient.get()
                .uri("/api/products/{id}/availability", id)
                .retrieve()
                .body(Boolean.class);
    }
}