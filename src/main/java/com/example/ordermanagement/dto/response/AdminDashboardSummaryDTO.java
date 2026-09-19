package com.example.ordermanagement.dto.response;

import java.math.BigDecimal;

public class AdminDashboardSummaryDTO {

    private long totalProducts;
    private long totalOrders;
    private BigDecimal totalRevenue;
    private long lowStockCount;
    private long pendingOrdersCount;

    public AdminDashboardSummaryDTO() {
    }

    public AdminDashboardSummaryDTO(long totalProducts, long totalOrders, BigDecimal totalRevenue, long lowStockCount, long pendingOrdersCount) {
        this.totalProducts = totalProducts;
        this.totalOrders = totalOrders;
        this.totalRevenue = totalRevenue;
        this.lowStockCount = lowStockCount;
        this.pendingOrdersCount = pendingOrdersCount;
    }

    public static Builder builder() {
        return new Builder();
    }

    public long getTotalProducts() {
        return totalProducts;
    }

    public void setTotalProducts(long totalProducts) {
        this.totalProducts = totalProducts;
    }

    public long getTotalOrders() {
        return totalOrders;
    }

    public void setTotalOrders(long totalOrders) {
        this.totalOrders = totalOrders;
    }

    public BigDecimal getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(BigDecimal totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    public long getLowStockCount() {
        return lowStockCount;
    }

    public void setLowStockCount(long lowStockCount) {
        this.lowStockCount = lowStockCount;
    }

    public long getPendingOrdersCount() {
        return pendingOrdersCount;
    }

    public void setPendingOrdersCount(long pendingOrdersCount) {
        this.pendingOrdersCount = pendingOrdersCount;
    }

    public static class Builder {
        private long totalProducts;
        private long totalOrders;
        private BigDecimal totalRevenue;
        private long lowStockCount;
        private long pendingOrdersCount;

        public Builder totalProducts(long totalProducts) {
            this.totalProducts = totalProducts;
            return this;
        }

        public Builder totalOrders(long totalOrders) {
            this.totalOrders = totalOrders;
            return this;
        }

        public Builder totalRevenue(BigDecimal totalRevenue) {
            this.totalRevenue = totalRevenue;
            return this;
        }

        public Builder lowStockCount(long lowStockCount) {
            this.lowStockCount = lowStockCount;
            return this;
        }

        public Builder pendingOrdersCount(long pendingOrdersCount) {
            this.pendingOrdersCount = pendingOrdersCount;
            return this;
        }

        public AdminDashboardSummaryDTO build() {
            return new AdminDashboardSummaryDTO(totalProducts, totalOrders, totalRevenue, lowStockCount, pendingOrdersCount);
        }
    }
}
