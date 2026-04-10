package com.project.orderinventorymanagement.store.controller;


import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.*;

import com.project.orderinventorymanagement.store.dto.InventoryDTO;
import com.project.orderinventorymanagement.store.entity.Inventory;
import com.project.orderinventorymanagement.store.service.InventoryService;

 

@RestController
@RequestMapping("/inventory")
public class InventoryController {

    private final InventoryService service;

    public InventoryController(InventoryService service) {
        this.service = service;
    }
    
 
    @GetMapping("/product/{productId}")
    public List<InventoryDTO> getStockByProduct(@PathVariable Integer productId) {
        List<Inventory> entities = service.getStockByProduct(productId);
        return entities.stream()
                       .map(this::convertToDTO)
                       .collect(Collectors.toList());
    }
    
    @GetMapping("/store/{storeId}")
    public List<InventoryDTO> getProductsByStore(@PathVariable Integer storeId) {
        List<Inventory> entities = service.getProductsByStore(storeId);
        return entities.stream()
                       .map(this::convertToDTO)
                       .collect(Collectors.toList());
    }
    

    
    @PostMapping("/reduce")
    public String reduceStock(@RequestBody InventoryDTO dto) {
        service.reduceStock(dto);
        return "Stock reduced successfully";
    }

   
    @PostMapping("/add")
    public String addStock(@RequestBody InventoryDTO dto) {
        service.addStock(dto);
        return "Stock added successfully";
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