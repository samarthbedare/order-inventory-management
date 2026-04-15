package com.project.orderinventorymanagement.storeservice.repository;


import com.project.orderinventorymanagement.storeservice.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InventoryRepository extends JpaRepository<Inventory, Integer> {


    List<Inventory> findByProductProductId(Integer productId);

    Optional<Inventory> findByStoreStoreIdAndProductProductId(Integer storeId, Integer productId);

    List<Inventory> findByStoreStoreId(Integer storeId);
}
