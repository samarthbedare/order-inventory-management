package com.project.orderinventorymanagement.storeservice.service;


import com.project.orderinventorymanagement.storeservice.dto.InventoryDTO;
import com.project.orderinventorymanagement.storeservice.entity.Inventory;
import com.project.orderinventorymanagement.storeservice.entity.Store;
import com.project.orderinventorymanagement.productservice.entity.Product;
import com.project.orderinventorymanagement.storeservice.exception.InsufficientStockException;
import com.project.orderinventorymanagement.storeservice.exception.ResourceNotFoundException;
import com.project.orderinventorymanagement.storeservice.repository.InventoryRepository;
import com.project.orderinventorymanagement.storeservice.repository.StoreRepository;
import com.project.orderinventorymanagement.productservice.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class InventoryService {

    private final InventoryRepository repo;
    private final StoreRepository storeRepo;
    private final ProductRepository productRepo;

    public InventoryService(InventoryRepository repo, StoreRepository storeRepo, ProductRepository productRepo) {
        this.repo = repo;
        this.storeRepo = storeRepo;
        this.productRepo = productRepo;
    }

    public List<InventoryDTO> getStockByProduct(Integer productId) {
        List<Inventory> inventoryList = repo.findByProductProductId(productId);
        if (inventoryList.isEmpty()) {
            throw new ResourceNotFoundException("No inventory records found for Product ID: " + productId);
        }
        return inventoryList.stream().map(this::convertToDTO).collect(java.util.stream.Collectors.toList());
    }

    public List<InventoryDTO> getProductsByStore(Integer storeId) {
        List<Inventory> inventoryList = repo.findByStoreStoreId(storeId);
        if (inventoryList.isEmpty()) {
            throw new ResourceNotFoundException("No products found for Store ID: " + storeId);
        }
        return inventoryList.stream().map(this::convertToDTO).collect(java.util.stream.Collectors.toList());
    }

    @Transactional
    public InventoryDTO createInventoryRecord(InventoryDTO dto) {
        if (repo.findByStoreStoreIdAndProductProductId(dto.getStoreId(), dto.getProductId()).isPresent()) {
            throw new IllegalArgumentException("Inventory record already exists for this Store and Product combination.");
        }

        Store store = storeRepo.findById(dto.getStoreId())
                .orElseThrow(() -> new ResourceNotFoundException("Store not found with ID: " + dto.getStoreId()));
        Product product = productRepo.findById(dto.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + dto.getProductId()));

        Inventory inventory = new Inventory();
        inventory.setStore(store);
        inventory.setProduct(product);
        inventory.setProductInventory(dto.getQuantity() != null ? dto.getQuantity() : 0);

        return convertToDTO(repo.save(inventory));
    }

    @Transactional
    public InventoryDTO reduceStock(InventoryDTO dto) {
        Inventory inventory = repo.findByStoreStoreIdAndProductProductId(
                dto.getStoreId(),
                dto.getProductId()
        ).orElseThrow(() -> new ResourceNotFoundException("Inventory record not found"));

        if (inventory.getProductInventory() < dto.getQuantity()) {
            throw new InsufficientStockException("Not enough stock. Current stock is: " + inventory.getProductInventory());
        }

        inventory.setProductInventory(inventory.getProductInventory() - dto.getQuantity());
        return convertToDTO(repo.save(inventory));
    }

    @Transactional
    public InventoryDTO addStock(InventoryDTO dto) {
        Inventory inventory = repo.findByStoreStoreIdAndProductProductId(dto.getStoreId(), dto.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Inventory record not found for Store ID: " + dto.getStoreId() + 
                                " and Product ID: " + dto.getProductId()));

        inventory.setProductInventory(inventory.getProductInventory() + dto.getQuantity());
        return convertToDTO(repo.save(inventory));
    }

    @Transactional
    public void deleteInventory(Integer storeId, Integer productId) {
        Inventory inventory = repo.findByStoreStoreIdAndProductProductId(storeId, productId)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory record not found"));
        repo.delete(inventory);
    }

    private InventoryDTO convertToDTO(Inventory inventory) {
        InventoryDTO dto = new InventoryDTO();
        dto.setInventoryId(inventory.getInventoryId());
        dto.setStoreId(inventory.getStore().getStoreId());
        dto.setProductId(inventory.getProduct().getProductId());
        dto.setQuantity(inventory.getProductInventory());
        return dto;
    }
}
