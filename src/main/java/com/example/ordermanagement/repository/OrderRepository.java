package com.example.ordermanagement.repository;

import com.example.ordermanagement.entity.CustomerOrder;
import com.example.ordermanagement.entity.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<CustomerOrder, String> {

    List<CustomerOrder> findByCustomer_CustomerId(String customerId);
    long countByStatus(OrderStatus status);
}
