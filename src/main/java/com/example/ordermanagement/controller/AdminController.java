package com.example.ordermanagement.controller;

import com.example.ordermanagement.dto.request.CreateProductRequest;
import com.example.ordermanagement.dto.request.UpdateStockRequest;
import com.example.ordermanagement.dto.response.AdminDashboardSummaryDTO;
import com.example.ordermanagement.dto.response.OrderResponse;
import com.example.ordermanagement.dto.response.ProductResponse;
import com.example.ordermanagement.entity.OrderStatus;
import com.example.ordermanagement.service.OrderService;
import com.example.ordermanagement.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping({"/api/v1/admin", "/admin"})
@CrossOrigin(origins = "*")
public class AdminController {

    private final ProductService productService;
    private final OrderService orderService;

    public AdminController(ProductService productService, OrderService orderService) {
        this.productService = productService;
        this.orderService = orderService;
    }

    // ==========================================
    // 1. Admin Product / Item Management
    // ==========================================

    @PostMapping("/products")
    public ResponseEntity<ProductResponse> addProduct(@Valid @RequestBody CreateProductRequest request) {
        ProductResponse response = productService.createProduct(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/products/{productId}")
    public ResponseEntity<ProductResponse> updateProduct(@PathVariable String productId,
                                                         @Valid @RequestBody CreateProductRequest request) {
        ProductResponse response = productService.updateProduct(productId, request);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/products/{productId}/stock")
    public ResponseEntity<ProductResponse> updateProductStock(@PathVariable String productId,
                                                              @Valid @RequestBody UpdateStockRequest request) {
        ProductResponse response = productService.updateStock(productId, request.getStock());
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/products/{productId}")
    public ResponseEntity<Map<String, String>> deleteProduct(@PathVariable String productId) {
        productService.deleteProduct(productId);
        return ResponseEntity.ok(Map.of(
                "message", "Product deleted successfully",
                "productId", productId
        ));
    }

    @GetMapping("/products")
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        List<ProductResponse> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/products/{productId}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable String productId) {
        ProductResponse response = productService.getProductById(productId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/products/low-stock")
    public ResponseEntity<List<ProductResponse>> getLowStockProducts(
            @RequestParam(name = "threshold", defaultValue = "10") Integer threshold) {
        List<ProductResponse> lowStockProducts = productService.getLowStockProducts(threshold);
        return ResponseEntity.ok(lowStockProducts);
    }

    // ==========================================
    // 2. Admin Order Management
    // ==========================================

    @GetMapping("/orders")
    public ResponseEntity<List<OrderResponse>> getAllOrders() {
        List<OrderResponse> orders = orderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/orders/{orderId}")
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable String orderId) {
        OrderResponse order = orderService.getOrderById(orderId);
        return ResponseEntity.ok(order);
    }

    @PatchMapping("/orders/{orderId}/status")
    public ResponseEntity<OrderResponse> updateOrderStatus(@PathVariable String orderId,
                                                           @RequestParam(name = "status") OrderStatus status) {
        OrderResponse updatedOrder = orderService.updateOrderStatus(orderId, status);
        return ResponseEntity.ok(updatedOrder);
    }

    // ==========================================
    // 3. Admin Dashboard Summary
    // ==========================================

    @GetMapping("/dashboard/summary")
    public ResponseEntity<AdminDashboardSummaryDTO> getDashboardSummary(
            @RequestParam(name = "lowStockThreshold", defaultValue = "10") Integer lowStockThreshold) {
        long totalProducts = productService.countProducts();
        long lowStockCount = productService.countLowStockProducts(lowStockThreshold);
        long totalOrders = orderService.countOrders();
        long pendingOrdersCount = orderService.countPendingOrders();
        BigDecimal totalRevenue = orderService.calculateTotalRevenue();

        AdminDashboardSummaryDTO summary = AdminDashboardSummaryDTO.builder()
                .totalProducts(totalProducts)
                .totalOrders(totalOrders)
                .totalRevenue(totalRevenue)
                .lowStockCount(lowStockCount)
                .pendingOrdersCount(pendingOrdersCount)
                .build();

        return ResponseEntity.ok(summary);
    }
}
