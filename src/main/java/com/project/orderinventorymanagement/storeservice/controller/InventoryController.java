package com.project.orderinventorymanagement.storeservice.controller;


import com.project.orderinventorymanagement.storeservice.dto.InventoryDTO;
import com.project.orderinventorymanagement.storeservice.service.InventoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/inventory")
public class InventoryController {

    private final InventoryService service;

    public InventoryController(InventoryService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<InventoryDTO>> getAllInventory() {
        return ResponseEntity.ok(service.getAllInventory());
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<InventoryDTO>> getStockByProduct(@PathVariable Integer productId) {
        return ResponseEntity.ok(service.getStockByProduct(productId));
    }

    @GetMapping("/store/{storeId}")
    public ResponseEntity<List<InventoryDTO>> getProductsByStore(@PathVariable Integer storeId) {
        return ResponseEntity.ok(service.getProductsByStore(storeId));
    }

    @PostMapping
    public ResponseEntity<InventoryDTO> createInventoryRecord(@RequestBody InventoryDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createInventoryRecord(dto));
    }

    @PatchMapping("/reduce")
    public ResponseEntity<InventoryDTO> reduceStock(@RequestBody InventoryDTO dto) {
        return ResponseEntity.ok(service.reduceStock(dto));
    }

    @PatchMapping("/add")
    public ResponseEntity<InventoryDTO> addStock(@RequestBody InventoryDTO dto) {
        return ResponseEntity.ok(service.addStock(dto));
    }

    @DeleteMapping("/store/{storeId}/product/{productId}")
    public ResponseEntity<Void> deleteInventory(@PathVariable Integer storeId, @PathVariable Integer productId) {
        service.deleteInventory(storeId, productId);
        return ResponseEntity.noContent().build();
    }
}
