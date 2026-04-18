package com.project.orderinventorymanagement;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.orderinventorymanagement.customerservice.dto.CustomerDTO;
import com.project.orderinventorymanagement.orderservice.dto.OrderItemRequest;
import com.project.orderinventorymanagement.orderservice.dto.OrderRequest;
import com.project.orderinventorymanagement.orderservice.dto.OrderStatusUpdateDTO;
import com.project.orderinventorymanagement.orderservice.dto.OrderShipmentUpdateDTO;
import com.project.orderinventorymanagement.productservice.dto.ProductDTO;
import com.project.orderinventorymanagement.productservice.dto.ProductUpdateDTO;
import com.project.orderinventorymanagement.shippingservice.entity.ShipmentStatus;
import com.project.orderinventorymanagement.storeservice.dto.InventoryDTO;
import com.project.orderinventorymanagement.storeservice.dto.StoreDTO;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * SystemFlowIntegrationTest
 * 
 * Now with dynamic Admin registration for clean testing.
 */
@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SystemFlowIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    // Shared state across ordered tests
    private String adminToken;
    private String adminUser = "testAdmin_" + System.currentTimeMillis();
    private String adminPass = "testPass123";
    private Integer customerId;
    private Integer storeId;
    private Integer productId;
    private Integer orderId;
    private Integer shipmentId;
    private Integer manualShipmentId;
    private String customerEmail = "dynamic.secured." + System.currentTimeMillis() + "@test.com";

    @BeforeAll
    public void setup() throws Exception {
        // 1. Dynamic Signup
        Map<String, String> signupReq = Map.of("username", adminUser, "password", adminPass);
        mockMvc.perform(post("/api/v1/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signupReq)))
                .andExpect(status().isOk());

        // 2. Login to get Token
        Map<String, String> loginReq = Map.of("username", adminUser, "password", adminPass);
        MvcResult result = mockMvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginReq)))
                .andExpect(status().isOk())
                .andReturn();
        
        @SuppressWarnings("unchecked")
        Map<String, String> response = objectMapper.readValue(result.getResponse().getContentAsString(), Map.class);
        adminToken = response.get("token");
    }

    @Test
    @Order(1)
    @DisplayName("Phase 1: Environment Baseline")
    public void test01_Baselines() throws Exception {
        mockMvc.perform(get("/api/v1/products")).andExpect(status().isOk());
        mockMvc.perform(get("/api/v1/stores")).andExpect(status().isOk());
        
        mockMvc.perform(get("/api/v1/customers").header("Authorization", "Bearer " + adminToken)).andExpect(status().isOk());
        mockMvc.perform(get("/api/v1/orders").header("Authorization", "Bearer " + adminToken)).andExpect(status().isOk());
        mockMvc.perform(get("/api/v1/shipments").header("Authorization", "Bearer " + adminToken)).andExpect(status().isOk());
    }

    @Test
    @Order(2)
    @DisplayName("Phase 2: Customer Lifecycle (Secured)")
    public void test02_CustomerFlow() throws Exception {
        CustomerDTO customerReq = new CustomerDTO();
        customerReq.setFullName("Dynamic Secured John");
        customerReq.setEmailAddress(customerEmail);
        
        MvcResult cRes = mockMvc.perform(post("/api/v1/customers")
                .header("Authorization", "Bearer " + adminToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(customerReq)))
                .andExpect(status().isCreated())
                .andReturn();
        customerId = objectMapper.readValue(cRes.getResponse().getContentAsString(), CustomerDTO.class).getCustomerId();

        mockMvc.perform(get("/api/v1/customers/" + customerId).header("Authorization", "Bearer " + adminToken)).andExpect(status().isOk());
        mockMvc.perform(get("/api/v1/customers/email/" + customerEmail).header("Authorization", "Bearer " + adminToken)).andExpect(status().isOk());
        
        customerReq.setFullName("Dynamic Secured John Updated");
        mockMvc.perform(put("/api/v1/customers/" + customerId)
                .header("Authorization", "Bearer " + adminToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(customerReq)))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/v1/customers/validate/" + customerId).header("Authorization", "Bearer " + adminToken)).andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    @Order(3)
    @DisplayName("Phase 3: Store & Product Lifecycle")
    public void test03_StoreProductFlow() throws Exception {
        StoreDTO storeReq = new StoreDTO();
        storeReq.setStoreName("Secured Dynamic Store " + System.currentTimeMillis());
        storeReq.setPhysicalAddress("123 Auth Lane, CA");
        
        MvcResult sRes = mockMvc.perform(post("/api/v1/stores")
                .header("Authorization", "Bearer " + adminToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(storeReq)))
                .andExpect(status().isCreated())
                .andReturn();
        storeId = objectMapper.readValue(sRes.getResponse().getContentAsString(), StoreDTO.class).getStoreId();
        mockMvc.perform(get("/api/v1/stores/" + storeId)).andExpect(status().isOk());

        ProductDTO prodReq = new ProductDTO();
        prodReq.setProductName("Dynamic Shield " + System.currentTimeMillis());
        prodReq.setUnitPrice(new BigDecimal("99.99"));
        prodReq.setBrand("CyberSentinel");
        prodReq.setColour("Silver");
        prodReq.setSize("OS");
        prodReq.setRating(5);
        
        MvcResult pRes = mockMvc.perform(post("/api/v1/products")
                .header("Authorization", "Bearer " + adminToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(prodReq)))
                .andExpect(status().isCreated())
                .andReturn();
        productId = objectMapper.readValue(pRes.getResponse().getContentAsString(), ProductDTO.class).getProductId();
        mockMvc.perform(get("/api/v1/products/" + productId)).andExpect(status().isOk());

        ProductUpdateDTO prodUpdate = new ProductUpdateDTO();
        prodUpdate.setUnitPrice(new BigDecimal("105.50"));
        mockMvc.perform(patch("/api/v1/products/" + productId)
                .header("Authorization", "Bearer " + adminToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(prodUpdate)))
                .andExpect(status().isOk());
    }

    @Test
    @Order(4)
    @DisplayName("Phase 4: Inventory Management (Secured)")
    public void test04_InventoryFlow() throws Exception {
        InventoryDTO invReq = new InventoryDTO();
        invReq.setStoreId(storeId);
        invReq.setProductId(productId);
        invReq.setQuantity(50);
        
        mockMvc.perform(post("/api/v1/inventory")
                .header("Authorization", "Bearer " + adminToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invReq)))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/v1/inventory/product/" + productId).header("Authorization", "Bearer " + adminToken)).andExpect(status().isOk());
        mockMvc.perform(get("/api/v1/inventory/store/" + storeId).header("Authorization", "Bearer " + adminToken)).andExpect(status().isOk());

        invReq.setQuantity(10);
        mockMvc.perform(patch("/api/v1/inventory/add")
                .header("Authorization", "Bearer " + adminToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invReq)))
                .andExpect(status().isOk());

        invReq.setQuantity(5);
        mockMvc.perform(patch("/api/v1/inventory/reduce")
                .header("Authorization", "Bearer " + adminToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invReq)))
                .andExpect(status().isOk());
    }

    @Test
    @Order(5)
    @DisplayName("Phase 5: Order Processing (Secured)")
    public void test05_OrderFlow() throws Exception {
        OrderItemRequest itemReq = new OrderItemRequest();
        itemReq.setProductId(productId);
        itemReq.setQuantity(1);
        itemReq.setUnitPrice(new BigDecimal("105.50"));

        OrderRequest orderReq = new OrderRequest();
        orderReq.setCustomerId(customerId);
        orderReq.setStoreId(storeId);
        orderReq.setDeliveryAddress("777 Dynamic Road");
        orderReq.setItems(Collections.singletonList(itemReq));

        MvcResult oRes = mockMvc.perform(post("/api/v1/orders")
                .header("Authorization", "Bearer " + adminToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(orderReq)))
                .andExpect(status().isCreated())
                .andReturn();
        
        List<?> ordersList = objectMapper.readValue(oRes.getResponse().getContentAsString(), List.class);
        @SuppressWarnings("unchecked")
        Map<String, Object> orderMap = (Map<String, Object>) ordersList.get(0);
        orderId = (Integer) orderMap.get("orderId");

        mockMvc.perform(get("/api/v1/orders/" + orderId).header("Authorization", "Bearer " + adminToken)).andExpect(status().isOk());
        mockMvc.perform(get("/api/v1/orders/customer/" + customerId).header("Authorization", "Bearer " + adminToken)).andExpect(status().isOk());
        mockMvc.perform(get("/api/v1/orders/store/" + storeId).header("Authorization", "Bearer " + adminToken)).andExpect(status().isOk());

        OrderStatusUpdateDTO statusUpdate = new OrderStatusUpdateDTO();
        statusUpdate.setOrderStatus("PAID");
        mockMvc.perform(patch("/api/v1/orders/" + orderId + "/status")
                .header("Authorization", "Bearer " + adminToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(statusUpdate)))
                .andExpect(status().isOk());
    }

    @Test
    @Order(6)
    @DisplayName("Phase 6: Logistics & Shipments (Secured)")
    public void test06_LogisticsFlow() throws Exception {
        MvcResult shipRes = mockMvc.perform(get("/api/v1/shipments/customer/" + customerId).header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andReturn();
        List<?> ships = objectMapper.readValue(shipRes.getResponse().getContentAsString(), List.class);
        @SuppressWarnings("unchecked")
        Map<String, Object> sMap = (Map<String, Object>) ships.get(0);
        shipmentId = (Integer) sMap.get("shipmentId");

        mockMvc.perform(get("/api/v1/shipments/" + shipmentId).header("Authorization", "Bearer " + adminToken)).andExpect(status().isOk());
        mockMvc.perform(get("/api/v1/shipments/store/" + storeId).header("Authorization", "Bearer " + adminToken)).andExpect(status().isOk());

        mockMvc.perform(patch("/api/v1/shipments/" + shipmentId + "/status")
                .header("Authorization", "Bearer " + adminToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(ShipmentStatus.SHIPPED)))
                .andExpect(status().isOk());

        Map<String, Object> manualShipReq = Map.of(
            "storeId", storeId,
            "customerId", customerId,
            "deliveryAddress", "Dynamic Secured Phase Path",
            "shipmentStatus", "CREATED"
        );
        MvcResult manualRes = mockMvc.perform(post("/api/v1/shipments")
                .header("Authorization", "Bearer " + adminToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(manualShipReq)))
                .andExpect(status().isCreated())
                .andReturn();
        @SuppressWarnings("unchecked")
        Map<String, Object> mResMap = objectMapper.readValue(manualRes.getResponse().getContentAsString(), Map.class);
        manualShipmentId = (Integer) mResMap.get("shipmentId");

        OrderShipmentUpdateDTO linkReq = new OrderShipmentUpdateDTO();
        linkReq.setShipmentId(manualShipmentId);
        mockMvc.perform(patch("/api/v1/orders/" + orderId + "/shipment")
                .header("Authorization", "Bearer " + adminToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(linkReq)))
                .andExpect(status().isOk());
    }

    @Test
    @Order(7)
    @DisplayName("Phase 7: Security & Authorization Failures")
    public void test07_SecurityFailures() throws Exception {
        // 1. Unauthorized Access (No Token)
        mockMvc.perform(get("/api/v1/customers"))
                .andExpect(status().isForbidden());

        // 2. Invalid Token
        mockMvc.perform(get("/api/v1/orders")
                .header("Authorization", "Bearer invalid_token_here"))
                .andExpect(status().isForbidden());
    }

    @Test
    @Order(8)
    @DisplayName("Phase 8: Resource Not Found Failures")
    public void test08_NotFoundFailures() throws Exception {
        // 1. Customer Not Found
        mockMvc.perform(get("/api/v1/customers/999999")
                .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isNotFound());

        // 2. Product Not Found
        mockMvc.perform(get("/api/v1/products/999999")
                .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isNotFound());

        // 3. Order Not Found
        mockMvc.perform(get("/api/v1/orders/999999")
                .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(9)
    @DisplayName("Phase 9: Business Rules & Data Integrity Failures")
    public void test09_BusinessRuleFailures() throws Exception {
        // 1. Duplicate Customer Email
        CustomerDTO duplicateReq = new CustomerDTO();
        duplicateReq.setFullName("Duplicate John");
        duplicateReq.setEmailAddress(customerEmail); // Already created in test02

        mockMvc.perform(post("/api/v1/customers")
                .header("Authorization", "Bearer " + adminToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(duplicateReq)))
                .andExpect(status().isConflict());

        // 2. Insufficient Stock
        OrderItemRequest overLimitItem = new OrderItemRequest();
        overLimitItem.setProductId(productId);
        overLimitItem.setQuantity(1000); // We only added 50-5+10 = 55 in test04
        overLimitItem.setUnitPrice(new BigDecimal("105.50"));

        OrderRequest overLimitOrder = new OrderRequest();
        overLimitOrder.setCustomerId(customerId);
        overLimitOrder.setStoreId(storeId);
        overLimitOrder.setDeliveryAddress("Overlimit Blvd");
        overLimitOrder.setItems(Collections.singletonList(overLimitItem));

        mockMvc.perform(post("/api/v1/orders")
                .header("Authorization", "Bearer " + adminToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(overLimitOrder)))
                .andExpect(status().isConflict());

        // 3. Invalid Order Status Update
        Map<String, String> invalidStatus = Map.of("orderStatus", "NOT_A_REAL_STATUS");
        mockMvc.perform(patch("/api/v1/orders/" + orderId + "/status")
                .header("Authorization", "Bearer " + adminToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidStatus)))
                .andExpect(status().isBadRequest());
    }

    @AfterAll
    public void tearDown() throws Exception {
        System.out.println(">>> DYNAMIC SECURED CLEANUP START <<<");
        try {
            if (orderId != null) mockMvc.perform(delete("/api/v1/orders/" + orderId).header("Authorization", "Bearer " + adminToken));
            if (shipmentId != null) mockMvc.perform(delete("/api/v1/shipments/" + shipmentId).header("Authorization", "Bearer " + adminToken));
            if (manualShipmentId != null) mockMvc.perform(delete("/api/v1/shipments/" + manualShipmentId).header("Authorization", "Bearer " + adminToken));
            if (storeId != null && productId != null) mockMvc.perform(delete("/api/v1/inventory/store/" + storeId + "/product/" + productId).header("Authorization", "Bearer " + adminToken));
            if (productId != null) mockMvc.perform(delete("/api/v1/products/" + productId).header("Authorization", "Bearer " + adminToken));
            if (storeId != null) mockMvc.perform(delete("/api/v1/stores/" + storeId).header("Authorization", "Bearer " + adminToken));
            if (customerId != null) mockMvc.perform(delete("/api/v1/customers/" + customerId).header("Authorization", "Bearer " + adminToken));
            
            // Clean up admin user
            // Note: need to find id or just delete by username in a manual command if needed.
            // For now, these are small test records.
        } catch (Exception e) {
            System.err.println("Cleanup warning: " + e.getMessage());
        }
        System.out.println(">>> DYNAMIC SECURED CLEANUP END <<<");
    }
}
