package com.project.orderinventorymanagement.shippingservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.orderinventorymanagement.customerservice.service.CustomerService;
import com.project.orderinventorymanagement.security.AdminUserRepository;
import com.project.orderinventorymanagement.security.JwtAuthenticationFilter;
import com.project.orderinventorymanagement.security.JwtUtil;
import com.project.orderinventorymanagement.shippingservice.dto.ShipmentResponseDTO;
import com.project.orderinventorymanagement.shippingservice.service.ShipmentService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ShipmentController.class)
@AutoConfigureMockMvc(addFilters = false)
class ShipmentControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ShipmentService shipmentService;

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @MockBean
    private AdminUserRepository adminUserRepository;

    @MockBean
    private CustomerService customerService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGetAllShipments() throws Exception {
        ShipmentResponseDTO shipment = new ShipmentResponseDTO();
        shipment.setShipmentId(10);
        shipment.setCustomerId(1);

        Mockito.when(shipmentService.getAllShipments()).thenReturn(Collections.singletonList(shipment));

        mockMvc.perform(get("/api/v1/shipments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].shipmentId").value(10));
    }

    @Test
    void testGetShipmentById() throws Exception {
        ShipmentResponseDTO shipment = new ShipmentResponseDTO();
        shipment.setShipmentId(10);
        shipment.setCustomerId(1);

        Mockito.when(shipmentService.getShipmentById(10)).thenReturn(shipment);

        mockMvc.perform(get("/api/v1/shipments/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.shipmentId").value(10));
    }

    @Test
    void testDeleteShipment() throws Exception {
        mockMvc.perform(delete("/api/v1/shipments/10"))
                .andExpect(status().isNoContent());

        Mockito.verify(shipmentService, Mockito.times(1)).deleteShipment(10);
    }
}
