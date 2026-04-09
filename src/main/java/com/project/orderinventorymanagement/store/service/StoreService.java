package com.project.orderinventorymanagement.store.service;

 

 
import org.springframework.stereotype.Service;

import com.project.orderinventorymanagement.store.dto.StoreDTO;
import com.project.orderinventorymanagement.store.entity.Store;
import com.project.orderinventorymanagement.store.repository.StoreRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StoreService {

    private final StoreRepository repo;

    public StoreService(StoreRepository repo) {
        this.repo = repo;
    }

    public List<StoreDTO> getAllStores() {
        return repo.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public StoreDTO getStoreById(Integer id) {
        Store store = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Store not found"));

        return convertToDTO(store);
    }

    private StoreDTO convertToDTO(Store store) {
        StoreDTO dto = new StoreDTO();

        dto.setStoreId(store.getStoreId());
        dto.setStoreName(store.getStoreName());
        dto.setWebAddress(store.getWebAddress());
        dto.setPhysicalAddress(store.getPhysicalAddress());
        dto.setLatitude(store.getLatitude());
        dto.setLongitude(store.getLongitude());

        return dto;
    }
}
