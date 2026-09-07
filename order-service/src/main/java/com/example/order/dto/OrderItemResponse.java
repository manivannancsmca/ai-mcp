package com.example.order.dto;

import java.math.BigDecimal;

public class OrderItemResponse {
    private Long productId;
    private String productName;
    private BigDecimal unitPrice;
    private Integer quantity;
    private BigDecimal subTotal;

    // constructors, getters & setters
    public OrderItemResponse() {}
    public OrderItemResponse(Long productId, String productName, BigDecimal unitPrice,
                             Integer quantity, BigDecimal subTotal) {
        this.productId = productId;
        this.productName = productName;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
        this.subTotal = subTotal;
    }

    // getters & setters omitted for brevity – generate them
}