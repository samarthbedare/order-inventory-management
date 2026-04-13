package com.project.orderinventorymanagement.store.repository;


import com.project.orderinventorymanagement.store.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StoreRepository extends JpaRepository<Store, Integer> {
    List<Store> findByPhysicalAddressContainingIgnoreCase(String physicalAddress);
}