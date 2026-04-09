package com.project.orderinventorymanagement.store.repository;

 
 
import org.springframework.data.jpa.repository.JpaRepository;

import com.project.orderinventorymanagement.store.entity.Inventory;

import java.util.List;
import java.util.Optional;

public interface InventoryRepository extends JpaRepository<Inventory, Integer> {

    Optional<Inventory> findByStoreIdAndProductId(Integer storeId, Integer productId);
    List<Inventory> findByProductId(Integer productId);

    List<Inventory> findByStoreId(Integer storeId);
}
