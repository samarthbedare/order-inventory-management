package com.project.orderinventorymanagement.store.service;

 

 

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.orderinventorymanagement.store.dto.InventoryDTO;
import com.project.orderinventorymanagement.store.entity.Inventory;
import com.project.orderinventorymanagement.store.repository.InventoryRepository;

@Service
public class InventoryService {

    private final InventoryRepository repo;

    public InventoryService(InventoryRepository repo) {
        this.repo = repo;
    }

    // Get Inventory
    public Inventory getInventory(Integer storeId, Integer productId) {
        return repo.findByStoreIdAndProductId(storeId, productId)
                .orElseThrow(() -> new RuntimeException("Inventory not found"));
    }
    
    //  Get stock across all stores for a product
    public List<Inventory> getStockByProduct(Integer productId) {
        return repo.findByProductId(productId);
    }

    // Get all products in a store
    public List<Inventory> getProductsByStore(Integer storeId) {
        return repo.findByStoreId(storeId);
    }

    // Reduce Quantity (After Order)
    @Transactional
    public void reduceStock(InventoryDTO dto) {

        //Fetch inventory
        Inventory inventory = repo.findByStoreIdAndProductId(
                dto.getStoreId(),
                dto.getProductId()
        ).orElseThrow(() -> new RuntimeException("Product not found"));

        //Check stock
        if (inventory.getProductInventory() < dto.getQuantity()) {
            throw new RuntimeException("Insufficient stock");
        }

        //Reduce stock
        inventory.setProductInventory(
                inventory.getProductInventory() - dto.getQuantity()
        );

        //Save
        repo.save(inventory);
    }


    //  Add Quantity (Restock)
    @Transactional
    public void addStock(InventoryDTO dto) {

        Inventory inventory = getInventory(dto.getStoreId(), dto.getProductId());

        inventory.setProductInventory(
                inventory.getProductInventory() + dto.getQuantity()
        );

        repo.save(inventory);
    }
}
