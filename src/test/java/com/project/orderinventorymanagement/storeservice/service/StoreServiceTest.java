package com.project.orderinventorymanagement.storeservice.service;

import com.project.orderinventorymanagement.storeservice.dto.StoreDTO;
import com.project.orderinventorymanagement.storeservice.entity.Store;
import com.project.orderinventorymanagement.storeservice.exception.ResourceNotFoundException;
import com.project.orderinventorymanagement.storeservice.repository.StoreRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class StoreServiceTest {

    @Mock
    private StoreRepository repo;

    @InjectMocks
    private StoreService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllStores_ReturnsList() {
        Store store = new Store();
        store.setStoreId(1);
        when(repo.findAll()).thenReturn(List.of(store));
        List<StoreDTO> result = service.getAllStores();
        assertThat(result).hasSize(1);
    }

    @Test
    void getStoreById_Success() {
        Store store = new Store();
        store.setStoreId(1);
        when(repo.findById(1)).thenReturn(Optional.of(store));
        StoreDTO result = service.getStoreById(1);
        assertThat(result.getStoreId()).isEqualTo(1);
    }

    @Test
    void getStoreById_NotFound_ThrowsException() {
        when(repo.findById(1)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.getStoreById(1))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void searchByPhysicalAddress_Success() {
        Store store = new Store();
        when(repo.findByPhysicalAddressContainingIgnoreCase("Main")).thenReturn(List.of(store));
        List<StoreDTO> result = service.searchByPhysicalAddress("Main");
        assertThat(result).hasSize(1);
    }

    @Test
    void createStore_Valid_ReturnsDTO() {
        StoreDTO dto = new StoreDTO();
        dto.setStoreName("Test Store");
        dto.setPhysicalAddress("123 Street");
        when(repo.save(any())).thenAnswer(i -> i.getArgument(0));

        StoreDTO result = service.createStore(dto);
        assertThat(result.getStoreName()).isEqualTo("Test Store");
    }

    @Test
    void createStore_NoAddress_ThrowsException() {
        StoreDTO dto = new StoreDTO();
        dto.setStoreName("Test Store");
        assertThatThrownBy(() -> service.createStore(dto))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void updateStore_Success() {
        Store store = new Store();
        store.setStoreId(1);
        StoreDTO update = new StoreDTO();
        update.setStoreName("New Store Name");

        when(repo.findById(1)).thenReturn(Optional.of(store));
        when(repo.save(any())).thenReturn(store);

        StoreDTO result = service.updateStore(1, update);
        assertThat(result.getStoreName()).isEqualTo("New Store Name");
    }

    @Test
    void deleteStore_Success() {
        when(repo.existsById(1)).thenReturn(true);
        service.deleteStore(1);
        verify(repo).deleteById(1);
    }

    @Test
    void deleteStore_NotFound_ThrowsException() {
        when(repo.existsById(1)).thenReturn(false);
        assertThatThrownBy(() -> service.deleteStore(1))
                .isInstanceOf(ResourceNotFoundException.class);
    }
}
