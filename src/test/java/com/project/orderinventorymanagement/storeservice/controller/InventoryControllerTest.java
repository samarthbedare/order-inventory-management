package com.project.orderinventorymanagement.storeservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.orderinventorymanagement.storeservice.dto.InventoryDTO;
import com.project.orderinventorymanagement.storeservice.service.InventoryService;
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

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(InventoryController.class)
@AutoConfigureMockMvc(addFilters = false)
class InventoryControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private InventoryService inventoryService;

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @MockBean
    private AdminUserRepository adminUserRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testCreateInventoryRecord() throws Exception {
        InventoryDTO dto = new InventoryDTO();
        dto.setProductId(1);
        dto.setStoreId(1);
        dto.setQuantity(100);

        Mockito.when(inventoryService.createInventoryRecord(any(InventoryDTO.class))).thenReturn(dto);

        mockMvc.perform(post("/api/v1/inventory")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.quantity").value(100));
    }

    @Test
    void testAddStock() throws Exception {
        InventoryDTO dto = new InventoryDTO();
        dto.setProductId(1);
        dto.setStoreId(1);
        dto.setQuantity(50);

        InventoryDTO returnedDto = new InventoryDTO();
        returnedDto.setQuantity(150);

        Mockito.when(inventoryService.addStock(any(InventoryDTO.class))).thenReturn(returnedDto);

        mockMvc.perform(patch("/api/v1/inventory/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.quantity").value(150));
    }
}
