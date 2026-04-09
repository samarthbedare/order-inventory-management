package com.project.orderinventorymanagement.store.controller;


import java.util.List;

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
    public List<Inventory> getStockByProduct(@PathVariable Integer productId) {
        return service.getStockByProduct(productId);
    }

    
    @GetMapping("/store/{storeId}")
    public List<Inventory> getProductsByStore(@PathVariable Integer storeId) {
        return service.getProductsByStore(storeId);
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
}