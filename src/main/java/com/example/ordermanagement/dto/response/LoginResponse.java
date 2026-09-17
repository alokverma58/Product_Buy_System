package com.example.ordermanagement.dto.response;

public class LoginResponse {

    private String message;
    private CustomerResponse customer;

    public LoginResponse() {
    }

    public LoginResponse(String message, CustomerResponse customer) {
        this.message = message;
        this.customer = customer;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public CustomerResponse getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerResponse customer) {
        this.customer = customer;
    }

    public static class Builder {
        private String message;
        private CustomerResponse customer;

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public Builder customer(CustomerResponse customer) {
            this.customer = customer;
            return this;
        }

        public LoginResponse build() {
            return new LoginResponse(message, customer);
        }
    }
}
