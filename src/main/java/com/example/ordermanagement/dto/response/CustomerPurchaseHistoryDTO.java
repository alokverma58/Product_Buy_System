package com.example.ordermanagement.dto.response;

import com.example.ordermanagement.dto.request.AddressRequest;

import java.math.BigDecimal;
import java.util.List;

public class CustomerPurchaseHistoryDTO {

    private String customerId;
    private String name;
    private String email;
    private String phone;
    private AddressRequest address;
    private List<PurchasedProductDTO> products;

    public CustomerPurchaseHistoryDTO() {
    }

    public CustomerPurchaseHistoryDTO(String customerId, String name, String email, String phone, AddressRequest address, List<PurchasedProductDTO> products) {
        this.customerId = customerId;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.products = products;
    }

    public static Builder builder() {
        return new Builder();
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

    public List<PurchasedProductDTO> getProducts() {
        return products;
    }

    public void setProducts(List<PurchasedProductDTO> products) {
        this.products = products;
    }

    public static class PurchasedProductDTO {
        private String productId;
        private String productName;
        private String category;
        private BigDecimal price;
        private Integer quantity;
        private String purchaseDate;

        public PurchasedProductDTO() {
        }

        public PurchasedProductDTO(String productId, String productName, String category, BigDecimal price, Integer quantity, String purchaseDate) {
            this.productId = productId;
            this.productName = productName;
            this.category = category;
            this.price = price;
            this.quantity = quantity;
            this.purchaseDate = purchaseDate;
        }

        public static Builder builder() {
            return new Builder();
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

        public static class Builder {
            private String productId;
            private String productName;
            private String category;
            private BigDecimal price;
            private Integer quantity;
            private String purchaseDate;

            public Builder productId(String productId) {
                this.productId = productId;
                return this;
            }

            public Builder productName(String productName) {
                this.productName = productName;
                return this;
            }

            public Builder category(String category) {
                this.category = category;
                return this;
            }

            public Builder price(BigDecimal price) {
                this.price = price;
                return this;
            }

            public Builder quantity(Integer quantity) {
                this.quantity = quantity;
                return this;
            }

            public Builder purchaseDate(String purchaseDate) {
                this.purchaseDate = purchaseDate;
                return this;
            }

            public PurchasedProductDTO build() {
                return new PurchasedProductDTO(productId, productName, category, price, quantity, purchaseDate);
            }
        }
    }

    public static class Builder {
        private String customerId;
        private String name;
        private String email;
        private String phone;
        private AddressRequest address;
        private List<PurchasedProductDTO> products;

        public Builder customerId(String customerId) {
            this.customerId = customerId;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder phone(String phone) {
            this.phone = phone;
            return this;
        }

        public Builder address(AddressRequest address) {
            this.address = address;
            return this;
        }

        public Builder products(List<PurchasedProductDTO> products) {
            this.products = products;
            return this;
        }

        public CustomerPurchaseHistoryDTO build() {
            return new CustomerPurchaseHistoryDTO(customerId, name, email, phone, address, products);
        }
    }
}
