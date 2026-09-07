package com.example.mcpserver.tool;

import com.example.mcpserver.client.ProductServiceClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.mcp.annotation.McpTool;
import org.springframework.ai.mcp.annotation.McpToolParam;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;

@Service
public class ProductTools {
    private static final Logger log = LoggerFactory.getLogger(ProductTools.class);
    private final ProductServiceClient client;

    public ProductTools(ProductServiceClient client) {
        this.client = client;
    }

    @McpTool(description = "Get detailed information about a product by its numeric ID")
    public Map getProductById(
            @McpToolParam(description = "The product ID", required = true) Long id) {
        log.info("MCP tool getProductById called with id={}", id);
        return client.getProductById(id);
    }

    @McpTool(description = "Retrieve the full list of all products in the catalog")
    public List getAllProducts() {
        log.info("MCP tool getAllProducts called");
        return client.getAllProducts();
    }

    @McpTool(description = "Search products by name (partial match, case-insensitive)")
    public List searchProductsByName(
            @McpToolParam(description = "Name or part of the name to search for", required = true) String name) {
        log.info("MCP tool searchProductsByName called with name={}", name);
        return client.searchByName(name);
    }

    @McpTool(description = "Check whether a product is currently available (in stock)")
    public Boolean checkProductAvailability(
            @McpToolParam(description = "The product ID", required = true) Long id) {
        log.info("MCP tool checkProductAvailability called with id={}", id);
        return client.checkAvailability(id);
    }
}