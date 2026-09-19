package com.example.ordermanagement.service;

import com.example.ordermanagement.dto.request.AddressRequest;
import com.example.ordermanagement.dto.request.CreateOrderRequest;
import com.example.ordermanagement.dto.request.OrderItemRequest;
import com.example.ordermanagement.dto.response.CustomerPurchaseHistoryDTO;
import com.example.ordermanagement.dto.response.OrderItemResponse;
import com.example.ordermanagement.dto.response.OrderResponse;
import com.example.ordermanagement.entity.Address;
import com.example.ordermanagement.entity.Customer;
import com.example.ordermanagement.entity.CustomerOrder;
import com.example.ordermanagement.entity.OrderItem;
import com.example.ordermanagement.entity.OrderStatus;
import com.example.ordermanagement.entity.Product;
import com.example.ordermanagement.exception.CustomerNotFoundException;
import com.example.ordermanagement.exception.InsufficientStockException;
import com.example.ordermanagement.exception.OrderNotFoundException;
import com.example.ordermanagement.exception.ProductNotFoundException;
import com.example.ordermanagement.repository.CustomerRepository;
import com.example.ordermanagement.repository.OrderRepository;
import com.example.ordermanagement.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    private static final AtomicLong ORDER_ID_SEQUENCE = new AtomicLong(System.currentTimeMillis() % 1000000);

    public OrderService(CustomerRepository customerRepository,
                        ProductRepository productRepository,
                        OrderRepository orderRepository) {
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
    }

    @Transactional
    public OrderResponse createOrder(CreateOrderRequest request) {
        String custId = request.getEffectiveCustomerId();
        if (custId == null || custId.isBlank()) {
            throw new IllegalArgumentException("Customer ID cannot be blank");
        }

        List<OrderItemRequest> effectiveItems = request.getEffectiveItems();
        if (effectiveItems.isEmpty()) {
            throw new IllegalArgumentException("Order must contain at least one product item");
        }

        // Find or auto-register customer
        Customer customer = customerRepository.findById(custId)
                .orElseGet(() -> {
                    String name = request.getEffectiveCustomerName() != null ? request.getEffectiveCustomerName() : "Customer " + custId;
                    String email = request.getCustomer() != null && request.getCustomer().getEmail() != null
                            ? request.getCustomer().getEmail()
                            : custId.toLowerCase() + "@example.com";
                    String phone = request.getCustomer() != null && request.getCustomer().getPhone() != null
                            ? request.getCustomer().getPhone()
                            : "+91-9876543210";

                    Address address = null;
                    AddressRequest addrReq = request.getEffectiveAddress();
                    if (addrReq != null) {
                        address = Address.builder()
                                .street(addrReq.getStreet())
                                .city(addrReq.getCity())
                                .state(addrReq.getState())
                                .zipCode(addrReq.getZipCode())
                                .build();
                    }

                    Customer newCust = Customer.builder()
                            .customerId(custId)
                            .name(name)
                            .email(email)
                            .phone(phone)
                            .password("password123")
                            .address(address)
                            .build();
                    return customerRepository.save(newCust);
                });

        // Update details if supplied in request
        if (request.getEffectiveCustomerName() != null && !request.getEffectiveCustomerName().isBlank()) {
            customer.setName(request.getEffectiveCustomerName());
        }
        if (request.getCustomer() != null) {
            if (request.getCustomer().getEmail() != null) customer.setEmail(request.getCustomer().getEmail());
            if (request.getCustomer().getPhone() != null) customer.setPhone(request.getCustomer().getPhone());
            if (request.getCustomer().getAddress() != null) {
                AddressRequest ar = request.getCustomer().getAddress();
                if (customer.getAddress() == null) {
                    customer.setAddress(Address.builder()
                            .street(ar.getStreet())
                            .city(ar.getCity())
                            .state(ar.getState())
                            .zipCode(ar.getZipCode())
                            .build());
                } else {
                    customer.getAddress().setStreet(ar.getStreet());
                    customer.getAddress().setCity(ar.getCity());
                    customer.getAddress().setState(ar.getState());
                    customer.getAddress().setZipCode(ar.getZipCode());
                }
            }
        }
        customerRepository.save(customer);

        String generatedOrderId = "ORD" + ORDER_ID_SEQUENCE.incrementAndGet();

        CustomerOrder customerOrder = CustomerOrder.builder()
                .orderId(generatedOrderId)
                .customer(customer)
                .orderDate(LocalDateTime.now())
                .status(OrderStatus.CONFIRMED)
                .totalAmount(BigDecimal.ZERO)
                .items(new ArrayList<>())
                .build();

        BigDecimal runningTotal = BigDecimal.ZERO;

        // Loop through items
        for (OrderItemRequest itemRequest : effectiveItems) {
            Product product = productRepository.findById(itemRequest.getProductId())
                    .orElseThrow(() -> new ProductNotFoundException(
                            "Product with ID " + itemRequest.getProductId() + " not found"));

            if (product.getStock() < itemRequest.getQuantity()) {
                throw new InsufficientStockException(
                        "Insufficient stock for product " + product.getProductId() +
                                " (" + product.getProductName() + "). Available: " +
                                product.getStock() + ", Requested: " + itemRequest.getQuantity());
            }

            BigDecimal unitPrice = product.getPrice();
            BigDecimal subtotal = unitPrice.multiply(BigDecimal.valueOf(itemRequest.getQuantity()));
            runningTotal = runningTotal.add(subtotal);

            product.setStock(product.getStock() - itemRequest.getQuantity());
            productRepository.save(product);

            OrderItem orderItem = OrderItem.builder()
                    .product(product)
                    .quantity(itemRequest.getQuantity())
                    .price(unitPrice)
                    .subtotal(subtotal)
                    .build();

            customerOrder.addItem(orderItem);
        }

        customerOrder.setTotalAmount(runningTotal);
        CustomerOrder savedOrder = orderRepository.save(customerOrder);

        return mapToResponse(savedOrder);
    }

    @Transactional
    public OrderResponse cancelOrder(String orderId) {
        CustomerOrder order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order with ID " + orderId + " not found"));

        if (order.getStatus() == OrderStatus.CANCELLED) {
            throw new IllegalStateException("Order with ID " + orderId + " is already CANCELLED");
        }

        order.setStatus(OrderStatus.CANCELLED);

        for (OrderItem item : order.getItems()) {
            Product product = item.getProduct();
            product.setStock(product.getStock() + item.getQuantity());
            productRepository.save(product);
        }

        CustomerOrder updatedOrder = orderRepository.save(order);
        return mapToResponse(updatedOrder);
    }

    @Transactional(readOnly = true)
    public OrderResponse getOrderById(String orderId) {
        CustomerOrder order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order with ID " + orderId + " not found"));
        return mapToResponse(order);
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> getOrdersByCustomer(String customerId) {
        if (!customerRepository.existsById(customerId)) {
            throw new CustomerNotFoundException("Customer with ID " + customerId + " not found");
        }
        return orderRepository.findByCustomer_CustomerId(customerId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse updateOrderStatus(String orderId, OrderStatus newStatus) {
        CustomerOrder order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order with ID " + orderId + " not found"));
        order.setStatus(newStatus);
        CustomerOrder updatedOrder = orderRepository.save(order);
        return mapToResponse(updatedOrder);
    }

    @Transactional(readOnly = true)
    public long countOrders() {
        return orderRepository.count();
    }

    @Transactional(readOnly = true)
    public long countPendingOrders() {
        return orderRepository.countByStatus(OrderStatus.PENDING);
    }

    @Transactional(readOnly = true)
    public BigDecimal calculateTotalRevenue() {
        return orderRepository.findAll().stream()
                .filter(order -> order.getStatus() != OrderStatus.CANCELLED)
                .map(CustomerOrder::getTotalAmount)
                .filter(amount -> amount != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public OrderResponse mapToResponse(CustomerOrder order) {
        List<OrderItemResponse> itemResponses = order.getItems().stream()
                .map(item -> OrderItemResponse.builder()
                        .productId(item.getProduct().getProductId())
                        .productName(item.getProduct().getProductName())
                        .quantity(item.getQuantity())
                        .price(item.getPrice())
                        .subtotal(item.getSubtotal())
                        .build())
                .collect(Collectors.toList());

        // Build exact nested CustomerPurchaseHistoryDTO
        AddressRequest addrReq = null;
        if (order.getCustomer().getAddress() != null) {
            Address a = order.getCustomer().getAddress();
            addrReq = AddressRequest.builder()
                    .street(a.getStreet())
                    .city(a.getCity())
                    .state(a.getState())
                    .zipCode(a.getZipCode())
                    .build();
        }

        String purchaseDateStr = order.getOrderDate().toLocalDate().toString();
        List<CustomerPurchaseHistoryDTO.PurchasedProductDTO> purchasedProducts = order.getItems().stream()
                .map(item -> CustomerPurchaseHistoryDTO.PurchasedProductDTO.builder()
                        .productId(item.getProduct().getProductId())
                        .productName(item.getProduct().getProductName())
                        .category(item.getProduct().getCategory())
                        .price(item.getPrice())
                        .quantity(item.getQuantity())
                        .purchaseDate(purchaseDateStr)
                        .build())
                .collect(Collectors.toList());

        CustomerPurchaseHistoryDTO historyDTO = CustomerPurchaseHistoryDTO.builder()
                .customerId(order.getCustomer().getCustomerId())
                .name(order.getCustomer().getName())
                .email(order.getCustomer().getEmail())
                .phone(order.getCustomer().getPhone())
                .address(addrReq)
                .products(purchasedProducts)
                .build();

        return OrderResponse.builder()
                .orderId(order.getOrderId())
                .status(order.getStatus())
                .orderDate(order.getOrderDate())
                .totalAmount(order.getTotalAmount())
                .customer(historyDTO)
                .build();
    }
}
