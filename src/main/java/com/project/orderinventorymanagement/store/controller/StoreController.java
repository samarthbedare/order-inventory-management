package com.project.orderinventorymanagement.store.controller;
 
 
import org.springframework.web.bind.annotation.*;

import com.project.orderinventorymanagement.store.dto.StoreDTO;
import com.project.orderinventorymanagement.store.service.StoreService;

import java.util.List;

@RestController
@RequestMapping("/stores")
public class StoreController {

    private final StoreService service;

    public StoreController(StoreService service) {
        this.service = service;
    }

    @GetMapping
    public List<StoreDTO> getAllStores() {
        return service.getAllStores();
    }

    @GetMapping("/{id}")
    public StoreDTO getStore(@PathVariable Integer id) {
        return service.getStoreById(id);
    }
}