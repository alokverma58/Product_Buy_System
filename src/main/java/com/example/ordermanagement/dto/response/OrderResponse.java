package com.example.ordermanagement.dto.response;

import com.example.ordermanagement.entity.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class OrderResponse {

    private String orderId;
    private OrderStatus status;
    private LocalDateTime orderDate;
    private BigDecimal totalAmount;
    private CustomerPurchaseHistoryDTO customer;

    public OrderResponse() {
    }

    public OrderResponse(String orderId, OrderStatus status, LocalDateTime orderDate, BigDecimal totalAmount, CustomerPurchaseHistoryDTO customer) {
        this.orderId = orderId;
        this.status = status;
        this.orderDate = orderDate;
        this.totalAmount = totalAmount;
        this.customer = customer;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public CustomerPurchaseHistoryDTO getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerPurchaseHistoryDTO customer) {
        this.customer = customer;
    }

    public static class Builder {
        private String orderId;
        private OrderStatus status;
        private LocalDateTime orderDate;
        private BigDecimal totalAmount;
        private CustomerPurchaseHistoryDTO customer;

        public Builder orderId(String orderId) {
            this.orderId = orderId;
            return this;
        }

        public Builder status(OrderStatus status) {
            this.status = status;
            return this;
        }

        public Builder orderDate(LocalDateTime orderDate) {
            this.orderDate = orderDate;
            return this;
        }

        public Builder totalAmount(BigDecimal totalAmount) {
            this.totalAmount = totalAmount;
            return this;
        }

        public Builder customer(CustomerPurchaseHistoryDTO customer) {
            this.customer = customer;
            return this;
        }

        public OrderResponse build() {
            return new OrderResponse(orderId, status, orderDate, totalAmount, customer);
        }
    }
}
