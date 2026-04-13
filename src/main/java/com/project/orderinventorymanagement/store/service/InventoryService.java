package com.project.orderinventorymanagement.store.service;


import com.project.orderinventorymanagement.store.dto.InventoryDTO;
import com.project.orderinventorymanagement.store.entity.Inventory;
import com.project.orderinventorymanagement.store.exception.InsufficientStockException;
import com.project.orderinventorymanagement.store.exception.ResourceNotFoundException;
import com.project.orderinventorymanagement.store.repository.InventoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class InventoryService {

    private final InventoryRepository repo;

    public InventoryService(InventoryRepository repo) {
        this.repo = repo;
    }


    public List<Inventory> getStockByProduct(Integer productId) {
        List<Inventory> inventoryList = repo.findByProductProductId(productId);


        if (inventoryList.isEmpty()) {
            throw new ResourceNotFoundException("No inventory records found for Product ID: " + productId);
        }

        return inventoryList;
    }

    public List<Inventory> getProductsByStore(Integer storeId) {
        List<Inventory> inventoryList = repo.findByStoreStoreId(storeId);

        if (inventoryList.isEmpty()) {
            throw new ResourceNotFoundException("No products found for Store ID: " + storeId);
        }

        return inventoryList;
    }

    @Transactional
    public void reduceStock(InventoryDTO dto) {

        //Fetch inventory
        Inventory inventory = repo.findByStoreStoreIdAndProductProductId(
                dto.getStoreId(),
                dto.getProductId()
        ).orElseThrow(() -> new ResourceNotFoundException("Inventory record not found"));

        //Check stock
        if (inventory.getProductInventory() < dto.getQuantity()) {
            throw new InsufficientStockException("Not enough stock. Current stock is: " + inventory.getProductInventory());
        }

        //Reduce stock
        inventory.setProductInventory(
                inventory.getProductInventory() - dto.getQuantity()
        );

        //Save
        repo.save(inventory);
    }


    @Transactional
    public void addStock(InventoryDTO dto) {

        Inventory inventory = repo.findByStoreStoreIdAndProductProductId(dto.getStoreId(), dto.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Inventory record not found for Store ID: " + dto.getStoreId() +
                                " and Product ID: " + dto.getProductId()));


        inventory.setProductInventory(
                inventory.getProductInventory() + dto.getQuantity()
        );


        repo.save(inventory);
    }
}