package com.project.orderinventorymanagement.productservice.service;

import com.project.orderinventorymanagement.productservice.dto.ProductDTO;
import com.project.orderinventorymanagement.productservice.dto.ProductUpdateDTO;
import com.project.orderinventorymanagement.productservice.entity.Product;
import com.project.orderinventorymanagement.productservice.exception.ProductNotFoundException;
import com.project.orderinventorymanagement.productservice.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ProductServiceTest {

    @Mock
    private ProductRepository repo;

    @InjectMocks
    private ProductService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllProducts_ReturnsList() {
        Product product = new Product();
        product.setProductId(1);
        when(repo.findByFilters(any(), any(), any(), any()))
                .thenReturn(List.of(product));

        List<ProductDTO> result = service.getAllProducts(null, null, null, null);
        assertThat(result).hasSize(1);
    }

    @Test
    void getProductById_Success() {
        Product product = new Product();
        product.setProductId(1);
        when(repo.findById(1)).thenReturn(Optional.of(product));
        ProductDTO result = service.getProductById(1);
        assertThat(result.getProductId()).isEqualTo(1);
    }

    @Test
    void getProductById_NotFound_ThrowsException() {
        when(repo.findById(1)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.getProductById(1))
                .isInstanceOf(ProductNotFoundException.class);
    }

    @Test
    void addProduct_Success() {
        ProductDTO dto = new ProductDTO();
        dto.setProductName("Test Product");
        dto.setUnitPrice(new BigDecimal("100"));
        dto.setRating(5);
        when(repo.save(any())).thenAnswer(i -> i.getArgument(0));

        ProductDTO result = service.addProduct(dto);
        assertThat(result.getProductName()).isEqualTo("Test Product");
    }

    @Test
    void updateProduct_Success() {
        Product product = new Product();
        product.setProductId(1);
        ProductUpdateDTO update = new ProductUpdateDTO();
        update.setRating(5);

        when(repo.findById(1)).thenReturn(Optional.of(product));
        when(repo.save(any())).thenReturn(product);

        ProductDTO result = service.updateProduct(1, update);
        assertThat(result.getRating()).isEqualTo(5);
    }

    @Test
    void deleteProduct_Success() {
        when(repo.existsById(1)).thenReturn(true);
        service.deleteProduct(1);
        verify(repo).deleteById(1);
    }
}
