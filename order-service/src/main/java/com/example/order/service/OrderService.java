package com.example.order.service;

import com.example.order.client.ProductServiceClient;
import com.example.order.dto.*;
import com.example.order.entity.*;
import com.example.order.exception.BusinessException;
import com.example.order.exception.ResourceNotFoundException;
import com.example.order.repository.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class OrderService {

    private static final Logger log = LoggerFactory.getLogger(OrderService.class);

    private final OrderRepository orderRepository;
    private final ProductServiceClient productClient;

    public OrderService(OrderRepository orderRepository, ProductServiceClient productClient) {
        this.orderRepository = orderRepository;
        this.productClient = productClient;
    }

    @Transactional
    public OrderResponse createOrder(OrderRequest request) {
        Order order = new Order();
        order.setCustomerName(request.getCustomerName());
        order.setCustomerEmail(request.getCustomerEmail());
        order.setStatus(OrderStatus.PENDING);

        BigDecimal total = BigDecimal.ZERO;

        for (OrderItemRequest itemReq : request.getItems()) {
            Map<String, Object> product = productClient.getProductById(itemReq.getProductId());

            if (product == null) {
                throw new BusinessException("Product not found with id: " + itemReq.getProductId());
            }

            Boolean available = productClient.isProductAvailable(itemReq.getProductId());
            if (available == null || !available) {
                throw new BusinessException("Product is not available: " + product.get("name"));
            }

            BigDecimal price = new BigDecimal(product.get("price").toString());
            int qty = itemReq.getQuantity();
            BigDecimal subTotal = price.multiply(BigDecimal.valueOf(qty));

            OrderItem item = new OrderItem();
            item.setProductId(itemReq.getProductId());
            item.setProductName((String) product.get("name"));
            item.setUnitPrice(price);
            item.setQuantity(qty);
            item.setSubTotal(subTotal);

            order.addItem(item);
            total = total.add(subTotal);
        }

        order.setTotalAmount(total);
        order = orderRepository.save(order);

        log.info("Order created successfully with id={}", order.getId());
        return toResponse(order);
    }

    public OrderResponse getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "id", id));
        return toResponse(order);
    }

    public List<OrderResponse> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    public List<OrderResponse> getOrdersByCustomerEmail(String email) {
        return orderRepository.findByCustomerEmail(email).stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public OrderResponse updateStatus(Long id, OrderStatus status) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "id", id));
        order.setStatus(status);
        order = orderRepository.save(order);
        return toResponse(order);
    }

    // ========== Mapping ==========
    private OrderResponse toResponse(Order order) {
        List<OrderItemResponse> items = order.getItems().stream()
                .map(i -> new OrderItemResponse(
                        i.getProductId(),
                        i.getProductName(),
                        i.getUnitPrice(),
                        i.getQuantity(),
                        i.getSubTotal()))
                .toList();

        OrderResponse response = new OrderResponse();
        response.setId(order.getId());
        response.setCustomerName(order.getCustomerName());
        response.setCustomerEmail(order.getCustomerEmail());
        response.setStatus(order.getStatus());
        response.setTotalAmount(order.getTotalAmount());
        response.setCreatedAt(order.getCreatedAt());
        response.setItems(items);
        return response;
    }
}