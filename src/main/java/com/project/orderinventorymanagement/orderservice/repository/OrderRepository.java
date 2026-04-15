package com.project.orderinventorymanagement.orderservice.repository;


import com.project.orderinventorymanagement.orderservice.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {

    List<Order> findByCustomerId(Integer customerId);
    List<Order> findByStoreId(Integer storeId);
}
