package com.project.orderinventorymanagement.productservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.orderinventorymanagement.productservice.dto.ProductDTO;
import com.project.orderinventorymanagement.productservice.service.ProductService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import com.project.orderinventorymanagement.security.JwtUtil;
import com.project.orderinventorymanagement.security.JwtAuthenticationFilter;
import com.project.orderinventorymanagement.security.AdminUserRepository;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
@AutoConfigureMockMvc(addFilters = false)
class ProductControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @MockBean
    private AdminUserRepository adminUserRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGetAllProducts() throws Exception {
        ProductDTO prod = new ProductDTO();
        prod.setProductName("Jeans");
        
        Mockito.when(productService.getAllProducts(null, null, null, null)).thenReturn(Collections.singletonList(prod));

        mockMvc.perform(get("/api/v1/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].productName").value("Jeans"));
    }

    @Test
    void testGetProductById() throws Exception {
        ProductDTO prod = new ProductDTO();
        prod.setProductId(10);
        prod.setProductName("Shirt");

        Mockito.when(productService.getProductById(10)).thenReturn(prod);

        mockMvc.perform(get("/api/v1/products/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productName").value("Shirt"));
    }

    @Test
    void testCreateProduct() throws Exception {
        ProductDTO prod = new ProductDTO();
        prod.setProductName("New Product");

        Mockito.when(productService.addProduct(any(ProductDTO.class))).thenReturn(prod);

        mockMvc.perform(post("/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(prod)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.productName").value("New Product"));
    }

    @Test
    void testUpdateProduct() throws Exception {
        com.project.orderinventorymanagement.productservice.dto.ProductUpdateDTO prodUpdate = 
                new com.project.orderinventorymanagement.productservice.dto.ProductUpdateDTO();

        ProductDTO prodReturned = new ProductDTO();
        prodReturned.setProductName("Updated");

        Mockito.when(productService.updateProduct(eq(10), any(com.project.orderinventorymanagement.productservice.dto.ProductUpdateDTO.class))).thenReturn(prodReturned);

        mockMvc.perform(patch("/api/v1/products/10")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(prodUpdate)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productName").value("Updated"));
    }

    @Test
    void testDeleteProduct() throws Exception {
        mockMvc.perform(delete("/api/v1/products/10"))
                .andExpect(status().isNoContent());

        Mockito.verify(productService, Mockito.times(1)).deleteProduct(10);
    }
}
