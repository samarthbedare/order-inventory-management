package com.project.orderinventorymanagement.orderservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.orderinventorymanagement.orderservice.dto.OrderRequest;
import com.project.orderinventorymanagement.orderservice.dto.OrderResponse;
import com.project.orderinventorymanagement.orderservice.service.OrderService;
import com.project.orderinventorymanagement.security.AdminUserRepository;
import com.project.orderinventorymanagement.security.JwtAuthenticationFilter;
import com.project.orderinventorymanagement.security.JwtUtil;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderController.class)
@AutoConfigureMockMvc(addFilters = false)
class OrderControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @MockBean
    private AdminUserRepository adminUserRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testCreateOrder() throws Exception {
        OrderRequest req = new OrderRequest();
        req.setCustomerId(1);

        OrderResponse res = new OrderResponse();
        res.setOrderId(100);

        Mockito.when(orderService.createOrder(any(OrderRequest.class))).thenReturn(Collections.singletonList(res));

        mockMvc.perform(post("/api/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$[0].orderId").value(100));
    }

    @Test
    void testGetAllOrders() throws Exception {
        OrderResponse res = new OrderResponse();
        res.setOrderId(100);

        Mockito.when(orderService.getAllOrders()).thenReturn(Collections.singletonList(res));

        mockMvc.perform(get("/api/v1/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].orderId").value(100));
    }

    @Test
    void testGetOrderById() throws Exception {
        OrderResponse res = new OrderResponse();
        res.setOrderId(100);

        Mockito.when(orderService.getOrderById(100)).thenReturn(res);

        mockMvc.perform(get("/api/v1/orders/100"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderId").value(100));
    }

    @Test
    void testGetOrdersByStore() throws Exception {
        OrderResponse res = new OrderResponse();
        res.setStoreId(5);

        Mockito.when(orderService.getOrdersByStore(5)).thenReturn(Collections.singletonList(res));

        mockMvc.perform(get("/api/v1/orders/store/5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].storeId").value(5));
    }

    @Test
    void testDeleteOrder() throws Exception {
        mockMvc.perform(delete("/api/v1/orders/1"))
                .andExpect(status().isNoContent());

        Mockito.verify(orderService, Mockito.times(1)).deleteOrder(1);
    }
}
