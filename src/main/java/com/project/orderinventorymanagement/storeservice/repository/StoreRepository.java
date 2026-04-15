package com.project.orderinventorymanagement.storeservice.repository;


import com.project.orderinventorymanagement.storeservice.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StoreRepository extends JpaRepository<Store, Integer> {
    List<Store> findByPhysicalAddressContainingIgnoreCase(String physicalAddress);
}
