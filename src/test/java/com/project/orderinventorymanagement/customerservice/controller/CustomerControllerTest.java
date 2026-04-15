package com.project.orderinventorymanagement.customerservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.orderinventorymanagement.customerservice.dto.CustomerDTO;
import com.project.orderinventorymanagement.customerservice.service.CustomerService;
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

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CustomerController.class)
@AutoConfigureMockMvc(addFilters = false)
public class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerService customerService;

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @MockBean
    private AdminUserRepository adminUserRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testGetAllCustomers() throws Exception {
        CustomerDTO customer = new CustomerDTO();
        customer.setCustomerId(1);
        customer.setFullName("John Doe");

        Mockito.when(customerService.getAllCustomers()).thenReturn(Arrays.asList(customer));

        mockMvc.perform(get("/api/v1/customers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].fullName").value("John Doe"));
    }

    @Test
    public void testGetCustomerById() throws Exception {
        CustomerDTO customer = new CustomerDTO();
        customer.setCustomerId(1);
        customer.setFullName("Jane Doe");

        Mockito.when(customerService.getCustomer(1)).thenReturn(customer);

        mockMvc.perform(get("/api/v1/customers/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fullName").value("Jane Doe"));
    }

    @Test
    public void testGetCustomerByEmail() throws Exception {
        CustomerDTO customer = new CustomerDTO();
        customer.setCustomerId(1);
        customer.setEmailAddress("test@test.com");

        Mockito.when(customerService.getCustomerByEmail("test@test.com")).thenReturn(customer);

        mockMvc.perform(get("/api/v1/customers/email/test@test.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.emailAddress").value("test@test.com"));
    }

    @Test
    public void testCreateCustomer() throws Exception {
        CustomerDTO customer = new CustomerDTO();
        customer.setFullName("New User");

        Mockito.when(customerService.createCustomer(any(CustomerDTO.class))).thenReturn(customer);

        mockMvc.perform(post("/api/v1/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customer)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.fullName").value("New User"));
    }

    @Test
    public void testUpdateCustomer() throws Exception {
        CustomerDTO customer = new CustomerDTO();
        customer.setFullName("Updated User");

        Mockito.when(customerService.updateCustomer(eq(1), any(CustomerDTO.class))).thenReturn(customer);

        mockMvc.perform(put("/api/v1/customers/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customer)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fullName").value("Updated User"));
    }

    @Test
    public void testDeleteCustomer() throws Exception {
        mockMvc.perform(delete("/api/v1/customers/1"))
                .andExpect(status().isNoContent());

        Mockito.verify(customerService, Mockito.times(1)).deleteCustomer(1);
    }
}
