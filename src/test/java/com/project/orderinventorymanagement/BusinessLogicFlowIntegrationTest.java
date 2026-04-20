package com.project.orderinventorymanagement;

import com.project.orderinventorymanagement.customerservice.dto.CustomerDTO;
import com.project.orderinventorymanagement.customerservice.service.CustomerService;
import com.project.orderinventorymanagement.orderservice.dto.OrderItemRequest;
import com.project.orderinventorymanagement.orderservice.dto.OrderRequest;
import com.project.orderinventorymanagement.orderservice.dto.OrderResponse;
import com.project.orderinventorymanagement.orderservice.service.OrderService;
import com.project.orderinventorymanagement.productservice.dto.ProductDTO;
import com.project.orderinventorymanagement.productservice.service.ProductService;
import com.project.orderinventorymanagement.shippingservice.dto.ShipmentResponseDTO;
import com.project.orderinventorymanagement.shippingservice.service.ShipmentService;
import com.project.orderinventorymanagement.storeservice.dto.InventoryDTO;
import com.project.orderinventorymanagement.storeservice.dto.StoreDTO;
import com.project.orderinventorymanagement.storeservice.service.InventoryService;
import com.project.orderinventorymanagement.storeservice.service.StoreService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@Transactional
class BusinessLogicFlowIntegrationTest {


    @Autowired
    private CustomerService customerService;

    @Autowired
    private StoreService storeService;

    @Autowired
    private ProductService productService;

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private ShipmentService shipmentService;

    @Test
    void testCompleteEcommerceLifecycle() {
        // 1. Create a Fake Environment Customer
        CustomerDTO customerReq = new CustomerDTO();
        customerReq.setFullName("E2E Test Customer");
        customerReq.setEmailAddress("e2e@testdomain.com");
        CustomerDTO customer = customerService.createCustomer(customerReq);

        // 2. Create a Fake Store Location
        StoreDTO storeReq = new StoreDTO();
        storeReq.setStoreName("E2E Hypermarket");
        storeReq.setPhysicalAddress("123 Test Ave");
        StoreDTO store = storeService.createStore(storeReq);

        // 3. Create a Fake Product
        ProductDTO prodReq = new ProductDTO();
        prodReq.setProductName("E2E Test Gadget");
        prodReq.setUnitPrice(new BigDecimal("50.00"));
        prodReq.setBrand("E2E Corp");
        prodReq.setColour("Black");
        prodReq.setSize("M");
        prodReq.setRating(5);
        ProductDTO product = productService.addProduct(prodReq);

        // 4. Bind Product to Store inside Inventory
        InventoryDTO invReq = new InventoryDTO();
        invReq.setStoreId(store.getStoreId());
        invReq.setProductId(product.getProductId());
        invReq.setQuantity(100);
        inventoryService.createInventoryRecord(invReq);

        // -> Validate starting stock
        List<InventoryDTO> startingStock = inventoryService.getStockByProduct(product.getProductId());
        assertEquals(100, startingStock.get(0).getQuantity(), "Initial stock must be exactly 100.");

        // 5. Customer Executes Monolithic Checkout
        OrderItemRequest itemReq = new OrderItemRequest();
        itemReq.setProductId(product.getProductId());
        itemReq.setQuantity(5);
        itemReq.setUnitPrice(new BigDecimal("250.00"));
        
        OrderRequest orderReq = new OrderRequest();
        orderReq.setCustomerId(customer.getCustomerId());
        orderReq.setStoreId(store.getStoreId());
        orderReq.setDeliveryAddress("456 Customer House");
        orderReq.setItems(Collections.singletonList(itemReq));
        
        List<OrderResponse> orders = orderService.createOrder(orderReq);
        assertNotNull(orders);
        assertEquals(1, orders.size());

        // 6. Assert Monolithic Output Mathematics
        OrderResponse completedOrder = orders.get(0);
        assertEquals("OPEN", completedOrder.getOrderStatus().name());
        assertEquals(new BigDecimal("250.00"), completedOrder.getItem().getUnitPrice(), "5 Gadgets * 50.00 should be 250.00 exact!");

        // 7. Validate Inventory Stock Reduction
        List<InventoryDTO> endingStock = inventoryService.getStockByProduct(product.getProductId());
        assertEquals(95, endingStock.get(0).getQuantity(), "5 Gadgets were ordered, stock MUST mathematically equal 95.");

        // 8. Validate Shipment Engine Trigger
        List<ShipmentResponseDTO> generatedShipments = shipmentService.getShipmentsByCustomer(customer.getCustomerId());
        assertFalse(generatedShipments.isEmpty(), "A shipment must have been triggered and mapped!");
        ShipmentResponseDTO latestShipment = generatedShipments.get(0);
        
        assertEquals(store.getStoreId(), latestShipment.getStoreId(), "Shipment must dispatch from hypermarket.");
        assertEquals("CREATED", latestShipment.getShipmentStatus().name(), "Default logistics status should be CREATED.");
    }
}
