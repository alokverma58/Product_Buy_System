package com.example.ordermanagement.repository;

import com.example.ordermanagement.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {
    List<Product> findByStockLessThanEqual(Integer stock);
    long countByStockLessThanEqual(Integer stock);
}
