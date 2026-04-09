package com.project.orderinventorymanagement.store.repository;

 

 
import org.springframework.data.jpa.repository.JpaRepository;

import com.project.orderinventorymanagement.store.entity.Store;

public interface StoreRepository extends JpaRepository<Store, Integer> {
}