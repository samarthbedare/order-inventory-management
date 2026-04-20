package com.project.orderinventorymanagement.productservice.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.project.orderinventorymanagement.productservice.entity.Product;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer> {

    @Query("SELECT p FROM Product p WHERE " +
           "(:name IS NULL OR :name = '' OR LOWER(p.productName) LIKE LOWER(CONCAT('%', :name, '%'))) AND " +
           "(:brand IS NULL OR :brand = '' OR LOWER(p.brand) LIKE LOWER(CONCAT('%', :brand, '%'))) AND " +
           "(:colour IS NULL OR :colour = '' OR LOWER(p.colour) LIKE LOWER(CONCAT('%', :colour, '%'))) AND " +
           "(:size IS NULL OR :size = '' OR LOWER(p.size) = LOWER(:size))")
    List<Product> findByFilters(
            @Param("name") String name,
            @Param("brand") String brand,
            @Param("colour") String colour,
            @Param("size") String size
    );

}
