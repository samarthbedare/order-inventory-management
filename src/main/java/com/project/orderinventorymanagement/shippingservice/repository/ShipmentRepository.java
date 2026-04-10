package com.project.orderinventorymanagement.shippingservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.orderinventorymanagement.shippingservice.entity.Shipment;



@Repository
public interface ShipmentRepository extends JpaRepository<Shipment, Integer> {
    List<Shipment> findByCustomerCustomerId(Integer customerId);
}