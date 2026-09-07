package com.example.mcpserver.tool;

import com.example.mcpserver.client.OrderServiceClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.mcp.annotation.McpTool;
import org.springframework.ai.mcp.annotation.McpToolParam;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class OrderTools {

    private static final Logger log = LoggerFactory.getLogger(OrderTools.class);
    private final OrderServiceClient client;

    public OrderTools(OrderServiceClient client) {
        this.client = client;
    }

    @McpTool(description = "Get details of an order by its ID")
    public Map<String, Object> getOrderById(
            @McpToolParam(description = "Order ID", required = true) Long id) {
        log.info("MCP tool getOrderById called with id={}", id);
        return client.getOrderById(id);
    }

    @McpTool(description = "Get all orders in the system")
    public List<Map<String, Object>> getAllOrders() {
        log.info("MCP tool getAllOrders called");
        return client.getAllOrders();
    }

    @McpTool(description = "Get all orders belonging to a customer by email")
    public List<Map<String, Object>> getOrdersByCustomerEmail(
            @McpToolParam(description = "Customer email address", required = true) String email) {
        log.info("MCP tool getOrdersByCustomerEmail called with email={}", email);
        return client.getOrdersByCustomer(email);
    }
}