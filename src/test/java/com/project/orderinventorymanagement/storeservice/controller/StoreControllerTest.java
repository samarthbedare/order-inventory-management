package com.project.orderinventorymanagement.storeservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.orderinventorymanagement.storeservice.dto.StoreDTO;
import com.project.orderinventorymanagement.storeservice.service.StoreService;
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

@WebMvcTest(StoreController.class)
@AutoConfigureMockMvc(addFilters = false)
public class StoreControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StoreService storeService;

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @MockBean
    private AdminUserRepository adminUserRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testGetAllStores() throws Exception {
        StoreDTO store = new StoreDTO();
        store.setStoreId(1);
        store.setStoreName("Test Store");

        Mockito.when(storeService.getAllStores()).thenReturn(Collections.singletonList(store));

        mockMvc.perform(get("/api/v1/stores"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].storeName").value("Test Store"));
    }

    @Test
    public void testCreateStore() throws Exception {
        StoreDTO store = new StoreDTO();
        store.setStoreName("New Store");

        Mockito.when(storeService.createStore(any(StoreDTO.class))).thenReturn(store);

        mockMvc.perform(post("/api/v1/stores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(store)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.storeName").value("New Store"));
    }

    @Test
    public void testDeleteStore() throws Exception {
        mockMvc.perform(delete("/api/v1/stores/1"))
                .andExpect(status().isNoContent());

        Mockito.verify(storeService, Mockito.times(1)).deleteStore(1);
    }
}
