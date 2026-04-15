package com.project.orderinventorymanagement.productservice.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.project.orderinventorymanagement.productservice.entity.Product;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer> {

    List<Product> findByProductNameContainingIgnoreCaseAndBrandContainingIgnoreCaseAndColourContainingIgnoreCaseAndSizeContainingIgnoreCase(
            String productName,
            String brand,
            String colour,
            String size
    );
}
