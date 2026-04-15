package com.project.orderinventorymanagement.customerservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.orderinventorymanagement.customerservice.entity.Customer;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {
    boolean existsByEmailAddress(String emailAddress);
    Optional<Customer> findByEmailAddress(String emailAddress);
}
