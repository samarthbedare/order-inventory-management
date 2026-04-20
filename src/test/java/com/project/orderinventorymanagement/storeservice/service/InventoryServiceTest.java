package com.project.orderinventorymanagement.storeservice.service;

import com.project.orderinventorymanagement.productservice.entity.Product;
import com.project.orderinventorymanagement.productservice.repository.ProductRepository;
import com.project.orderinventorymanagement.storeservice.dto.InventoryDTO;
import com.project.orderinventorymanagement.storeservice.entity.Inventory;
import com.project.orderinventorymanagement.storeservice.entity.Store;
import com.project.orderinventorymanagement.storeservice.exception.InsufficientStockException;
import com.project.orderinventorymanagement.storeservice.repository.InventoryRepository;
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

class InventoryServiceTest {

    @Mock
    private InventoryRepository inventoryRepository;
    @Mock
    private StoreRepository storeRepository;
    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private InventoryService inventoryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getStockByProduct_Success() {
        Inventory inv = new Inventory();
        inv.setStore(new Store());
        inv.setProduct(new Product());
        when(inventoryRepository.findByProductProductId(1)).thenReturn(List.of(inv));
        assertThat(inventoryService.getStockByProduct(1)).hasSize(1);
    }

    @Test
    void getProductsByStore_Success() {
        Inventory inv = new Inventory();
        inv.setStore(new Store());
        inv.setProduct(new Product());
        when(inventoryRepository.findByStoreStoreId(1)).thenReturn(List.of(inv));
        assertThat(inventoryService.getProductsByStore(1)).hasSize(1);
    }

    @Test
    void createInventoryRecord_Success() {
        InventoryDTO dto = new InventoryDTO();
        dto.setStoreId(1);
        dto.setProductId(1);
        dto.setQuantity(10);

        Store store = new Store();
        Product product = new Product();

        when(storeRepository.findById(1)).thenReturn(Optional.of(store));
        when(productRepository.findById(1)).thenReturn(Optional.of(product));
        when(inventoryRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        InventoryDTO result = inventoryService.createInventoryRecord(dto);
        assertThat(result.getQuantity()).isEqualTo(10);
    }

    @Test
    void reduceStock_Success() {
        InventoryDTO dto = new InventoryDTO();
        dto.setStoreId(1);
        dto.setProductId(1);
        dto.setQuantity(5);

        Inventory inventory = new Inventory();
        inventory.setStore(new Store());
        inventory.setProduct(new Product());
        inventory.setProductInventory(10);

        when(inventoryRepository.findByStoreStoreIdAndProductProductId(1, 1)).thenReturn(Optional.of(inventory));
        when(inventoryRepository.save(any())).thenReturn(inventory);

        inventoryService.reduceStock(dto);
        assertThat(inventory.getProductInventory()).isEqualTo(5);
    }

    @Test
    void reduceStock_Insufficient_ThrowsException() {
        InventoryDTO dto = new InventoryDTO();
        dto.setStoreId(1);
        dto.setProductId(1);
        dto.setQuantity(15);

        Inventory inventory = new Inventory();
        inventory.setProductInventory(10);

        when(inventoryRepository.findByStoreStoreIdAndProductProductId(1, 1)).thenReturn(Optional.of(inventory));

        assertThatThrownBy(() -> inventoryService.reduceStock(dto))
                .isInstanceOf(InsufficientStockException.class);
    }

    @Test
    void addStock_Success() {
        InventoryDTO dto = new InventoryDTO();
        dto.setStoreId(1);
        dto.setProductId(1);
        dto.setQuantity(5);

        Inventory inventory = new Inventory();
        inventory.setStore(new Store());
        inventory.setProduct(new Product());
        inventory.setProductInventory(10);

        when(inventoryRepository.findByStoreStoreIdAndProductProductId(1, 1)).thenReturn(Optional.of(inventory));
        when(inventoryRepository.save(any())).thenReturn(inventory);

        inventoryService.addStock(dto);
        assertThat(inventory.getProductInventory()).isEqualTo(15);
    }

    @Test
    void deleteInventory_Success() {
        Inventory inventory = new Inventory();
        inventory.setStore(new Store());
        inventory.setProduct(new Product());
        when(inventoryRepository.findByStoreStoreIdAndProductProductId(1, 1)).thenReturn(Optional.of(inventory));
        inventoryService.deleteInventory(1, 1);
        verify(inventoryRepository).delete(inventory);
    }
}
