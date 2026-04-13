package com.project.orderinventorymanagement.store.service;


import com.project.orderinventorymanagement.store.dto.StoreDTO;
import com.project.orderinventorymanagement.store.entity.Store;
import com.project.orderinventorymanagement.store.exception.ResourceNotFoundException;
import com.project.orderinventorymanagement.store.repository.StoreRepository;
import org.springframework.stereotype.Service;

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
                .orElseThrow(() -> new ResourceNotFoundException("Store not found with ID :" + id));

        return convertToDTO(store);
    }


    public List<StoreDTO> searchByPhysicalAddress(String address) {
        List<Store> stores = repo.findByPhysicalAddressContainingIgnoreCase(address);

        if (stores.isEmpty()) {
            throw new ResourceNotFoundException("No store found at physical address: " + address);
        }

        return stores.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public StoreDTO createStore(StoreDTO dto) {

        if ((dto.getWebAddress() == null || dto.getWebAddress().isBlank()) &&
                (dto.getPhysicalAddress() == null || dto.getPhysicalAddress().isBlank())) {
            throw new IllegalArgumentException(
                    "❌ Store must have at least one address: provide 'webAddress' or 'physicalAddress'."
            );
        }
        Store store = new Store();
        // Map DTO to Entity
        store.setStoreName(dto.getStoreName());
        store.setWebAddress(dto.getWebAddress());
        store.setPhysicalAddress(dto.getPhysicalAddress());
        store.setLatitude(dto.getLatitude());
        store.setLongitude(dto.getLongitude());
        // If you handle logo as byte[], set it here

        Store savedStore = repo.save(store);
        return convertToDTO(savedStore);
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