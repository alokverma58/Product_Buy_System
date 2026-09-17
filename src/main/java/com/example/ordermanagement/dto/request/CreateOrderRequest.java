package com.example.ordermanagement.dto.request;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class CreateOrderRequest {

    private String customerId;
    private String customerName;
    private List<OrderItemRequest> items;
    private AddressRequest shippingAddress;

    // Nested customer payload support
    private CustomerPayload customer;

    public CreateOrderRequest() {
    }

    public CreateOrderRequest(String customerId, String customerName, List<OrderItemRequest> items, AddressRequest shippingAddress, CustomerPayload customer) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.items = items;
        this.shippingAddress = shippingAddress;
        this.customer = customer;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getEffectiveCustomerId() {
        if (customerId != null && !customerId.isBlank()) {
            return customerId.trim();
        }
        if (customer != null && customer.getCustomerId() != null && !customer.getCustomerId().isBlank()) {
            return customer.getCustomerId().trim();
        }
        return null;
    }

    public String getEffectiveCustomerName() {
        if (customerName != null && !customerName.isBlank()) {
            return customerName.trim();
        }
        if (customer != null && customer.getName() != null && !customer.getName().isBlank()) {
            return customer.getName().trim();
        }
        return null;
    }

    public AddressRequest getEffectiveAddress() {
        if (shippingAddress != null) {
            return shippingAddress;
        }
        if (customer != null && customer.getAddress() != null) {
            return customer.getAddress();
        }
        return null;
    }

    public List<OrderItemRequest> getEffectiveItems() {
        if (items != null && !items.isEmpty()) {
            return items;
        }
        if (customer != null && customer.getProducts() != null && !customer.getProducts().isEmpty()) {
            List<OrderItemRequest> mappedItems = new ArrayList<>();
            for (ProductItemPayload p : customer.getProducts()) {
                int qty = p.getQuantity() != null ? p.getQuantity() : 1;
                mappedItems.add(new OrderItemRequest(p.getProductId(), qty));
            }
            return mappedItems;
        }
        return new ArrayList<>();
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getName() {
        return customerName;
    }

    public void setName(String name) {
        this.customerName = name;
    }

    public List<OrderItemRequest> getItems() {
        return items;
    }

    public void setItems(List<OrderItemRequest> items) {
        this.items = items;
    }

    public AddressRequest getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(AddressRequest shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public CustomerPayload getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerPayload customer) {
        this.customer = customer;
    }

    public static class CustomerPayload {
        private String customerId;
        private String name;
        private String email;
        private String phone;
        private AddressRequest address;
        private List<ProductItemPayload> products;

        public CustomerPayload() {
        }

        public CustomerPayload(String customerId, String name, String email, String phone, AddressRequest address, List<ProductItemPayload> products) {
            this.customerId = customerId;
            this.name = name;
            this.email = email;
            this.phone = phone;
            this.address = address;
            this.products = products;
        }

        public String getCustomerId() {
            return customerId;
        }

        public void setCustomerId(String customerId) {
            this.customerId = customerId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public AddressRequest getAddress() {
            return address;
        }

        public void setAddress(AddressRequest address) {
            this.address = address;
        }

        public List<ProductItemPayload> getProducts() {
            return products;
        }

        public void setProducts(List<ProductItemPayload> products) {
            this.products = products;
        }
    }

    public static class ProductItemPayload {
        private String productId;
        private String productName;
        private String category;
        private BigDecimal price;
        private Integer quantity;
        private String purchaseDate;

        public ProductItemPayload() {
        }

        public ProductItemPayload(String productId, String productName, String category, BigDecimal price, Integer quantity, String purchaseDate) {
            this.productId = productId;
            this.productName = productName;
            this.category = category;
            this.price = price;
            this.quantity = quantity;
            this.purchaseDate = purchaseDate;
        }

        public String getProductId() {
            return productId;
        }

        public void setProductId(String productId) {
            this.productId = productId;
        }

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public BigDecimal getPrice() {
            return price;
        }

        public void setPrice(BigDecimal price) {
            this.price = price;
        }

        public Integer getQuantity() {
            return quantity;
        }

        public void setQuantity(Integer quantity) {
            this.quantity = quantity;
        }

        public String getPurchaseDate() {
            return purchaseDate;
        }

        public void setPurchaseDate(String purchaseDate) {
            this.purchaseDate = purchaseDate;
        }
    }

    public static class Builder {
        private String customerId;
        private String customerName;
        private List<OrderItemRequest> items;
        private AddressRequest shippingAddress;
        private CustomerPayload customer;

        public Builder customerId(String customerId) {
            this.customerId = customerId;
            return this;
        }

        public Builder customerName(String customerName) {
            this.customerName = customerName;
            return this;
        }

        public Builder items(List<OrderItemRequest> items) {
            this.items = items;
            return this;
        }

        public Builder shippingAddress(AddressRequest shippingAddress) {
            this.shippingAddress = shippingAddress;
            return this;
        }

        public Builder customer(CustomerPayload customer) {
            this.customer = customer;
            return this;
        }

        public CreateOrderRequest build() {
            return new CreateOrderRequest(customerId, customerName, items, shippingAddress, customer);
        }
    }
}
