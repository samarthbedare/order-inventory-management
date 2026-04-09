package com.project.orderinventorymanagement.customerservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.orderinventorymanagement.customerservice.entity.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}