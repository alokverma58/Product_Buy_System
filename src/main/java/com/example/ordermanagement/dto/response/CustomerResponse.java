package com.example.ordermanagement.dto.response;

public class CustomerResponse {

    private String customerId;
    private String name;
    private String email;
    private String phone;
    private AddressResponse address;

    public CustomerResponse() {
    }

    public CustomerResponse(String customerId, String name, String email, String phone, AddressResponse address) {
        this.customerId = customerId;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
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

    public AddressResponse getAddress() {
        return address;
    }

    public void setAddress(AddressResponse address) {
        this.address = address;
    }

    public static class Builder {
        private String customerId;
        private String name;
        private String email;
        private String phone;
        private AddressResponse address;

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

        public Builder address(AddressResponse address) {
            this.address = address;
            return this;
        }

        public CustomerResponse build() {
            return new CustomerResponse(customerId, name, email, phone, address);
        }
    }

    public static class AddressResponse {
        private Long id;
        private String street;
        private String city;
        private String state;
        private String zipCode;

        public AddressResponse() {
        }

        public AddressResponse(Long id, String street, String city, String state, String zipCode) {
            this.id = id;
            this.street = street;
            this.city = city;
            this.state = state;
            this.zipCode = zipCode;
        }

        public static Builder builder() {
            return new Builder();
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getStreet() {
            return street;
        }

        public void setStreet(String street) {
            this.street = street;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getZipCode() {
            return zipCode;
        }

        public void setZipCode(String zipCode) {
            this.zipCode = zipCode;
        }

        public static class Builder {
            private Long id;
            private String street;
            private String city;
            private String state;
            private String zipCode;

            public Builder id(Long id) {
                this.id = id;
                return this;
            }

            public Builder street(String street) {
                this.street = street;
                return this;
            }

            public Builder city(String city) {
                this.city = city;
                return this;
            }

            public Builder state(String state) {
                this.state = state;
                return this;
            }

            public Builder zipCode(String zipCode) {
                this.zipCode = zipCode;
                return this;
            }

            public AddressResponse build() {
                return new AddressResponse(id, street, city, state, zipCode);
            }
        }
    }
}
