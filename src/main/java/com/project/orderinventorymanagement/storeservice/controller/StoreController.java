package com.project.orderinventorymanagement.storeservice.controller;


import com.project.orderinventorymanagement.storeservice.dto.StoreDTO;
import com.project.orderinventorymanagement.storeservice.service.StoreService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/stores")
public class StoreController {

    private final StoreService service;

    public StoreController(StoreService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<StoreDTO>> getAllStores() {
        return ResponseEntity.ok(service.getAllStores());
    }

    @GetMapping("/{id}")
    public ResponseEntity<StoreDTO> getStore(@PathVariable Integer id) {
        return ResponseEntity.ok(service.getStoreById(id));
    }


    @GetMapping("/search/address")
    public ResponseEntity<List<StoreDTO>> getByPhysicalAddress(@RequestParam("q") String address) {
        return ResponseEntity.ok(service.searchByPhysicalAddress(address));
    }

    @PostMapping
    public ResponseEntity<StoreDTO> addStore(@RequestBody StoreDTO storeDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createStore(storeDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<StoreDTO> updateStore(@PathVariable Integer id, @RequestBody StoreDTO dto) {
        return ResponseEntity.ok(service.updateStore(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStore(@PathVariable Integer id) {
        service.deleteStore(id);
        return ResponseEntity.noContent().build();
    }
}
