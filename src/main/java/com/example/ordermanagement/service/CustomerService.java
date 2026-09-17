package com.example.ordermanagement.service;

import com.example.ordermanagement.dto.request.AddressRequest;
import com.example.ordermanagement.dto.request.CreateCustomerRequest;
import com.example.ordermanagement.dto.request.LoginRequest;
import com.example.ordermanagement.dto.response.CustomerResponse;
import com.example.ordermanagement.dto.response.LoginResponse;
import com.example.ordermanagement.entity.Address;
import com.example.ordermanagement.entity.Customer;
import com.example.ordermanagement.exception.CustomerNotFoundException;
import com.example.ordermanagement.exception.InvalidCredentialsException;
import com.example.ordermanagement.repository.CustomerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Transactional
    public CustomerResponse createCustomer(CreateCustomerRequest request) {
        Address address = null;
        if (request.getAddress() != null) {
            AddressRequest addrReq = request.getAddress();
            address = Address.builder()
                    .street(addrReq.getStreet())
                    .city(addrReq.getCity())
                    .state(addrReq.getState())
                    .zipCode(addrReq.getZipCode())
                    .build();
        }

        String password = request.getPassword() != null && !request.getPassword().isBlank()
                ? request.getPassword()
                : "password123";

        Customer customer = Customer.builder()
                .customerId(request.getCustomerId())
                .name(request.getName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .password(password)
                .address(address)
                .build();

        Customer savedCustomer = customerRepository.save(customer);
        return mapToResponse(savedCustomer);
    }

    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest request) {
        String identifier = request.getIdentifier().trim();
        Customer customer = customerRepository.findByCustomerIdOrEmail(identifier, identifier)
                .orElseThrow(() -> new InvalidCredentialsException("Invalid Customer ID/Email or password"));

        if (customer.getPassword() == null || !customer.getPassword().equals(request.getPassword())) {
            throw new InvalidCredentialsException("Invalid Customer ID/Email or password");
        }

        return LoginResponse.builder()
                .message("Login successful")
                .customer(mapToResponse(customer))
                .build();
    }

    @Transactional(readOnly = true)
    public CustomerResponse getCustomerById(String customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("Customer with ID " + customerId + " not found"));
        return mapToResponse(customer);
    }

    @Transactional(readOnly = true)
    public List<CustomerResponse> getAllCustomers() {
        return customerRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public CustomerResponse mapToResponse(Customer customer) {
        CustomerResponse.AddressResponse addressResponse = null;
        if (customer.getAddress() != null) {
            Address addr = customer.getAddress();
            addressResponse = CustomerResponse.AddressResponse.builder()
                    .id(addr.getId())
                    .street(addr.getStreet())
                    .city(addr.getCity())
                    .state(addr.getState())
                    .zipCode(addr.getZipCode())
                    .build();
        }

        return CustomerResponse.builder()
                .customerId(customer.getCustomerId())
                .name(customer.getName())
                .email(customer.getEmail())
                .phone(customer.getPhone())
                .address(addressResponse)
                .build();
    }
}
