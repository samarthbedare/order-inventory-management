package com.project.orderinventorymanagement.orderservice.controller;


import com.project.orderinventorymanagement.orderservice.dto.OrderRequest;
import com.project.orderinventorymanagement.orderservice.dto.OrderResponse;
import com.project.orderinventorymanagement.orderservice.dto.OrderStatusUpdateDTO;
import com.project.orderinventorymanagement.orderservice.dto.OrderShipmentUpdateDTO;
import com.project.orderinventorymanagement.orderservice.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    // POST /orders
    @PostMapping
    public ResponseEntity<List<OrderResponse>> createOrder(@RequestBody OrderRequest request) {
        List<OrderResponse> response = orderService.createOrder(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // GET /orders
    @GetMapping
    public ResponseEntity<List<OrderResponse>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    // GET /orders/{id}
    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable Integer id) {
        return ResponseEntity.ok(orderService.getOrderById(id));
    }

    // GET /orders/store/{storeId}
    @GetMapping("/store/{storeId}")
    public ResponseEntity<List<OrderResponse>> getOrdersByStore(@PathVariable Integer storeId) {
        return ResponseEntity.ok(orderService.getOrdersByStore(storeId));
    }

    // GET /orders/customer/{customerId}
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<OrderResponse>> getOrdersByCustomer(@PathVariable Integer customerId) {
        List<OrderResponse> orders = orderService.getOrdersByCustomer(customerId);
        return ResponseEntity.ok(orders);
    }

    // PATCH /orders/{id}/status
    @PatchMapping("/{id}/status")
    public ResponseEntity<OrderResponse> updateStatus(
            @PathVariable Integer id,
            @RequestBody @jakarta.validation.Valid OrderStatusUpdateDTO dto) {
        String newStatus = dto.getOrderStatus();
        return ResponseEntity.ok(orderService.updateStatus(id, newStatus));
        // Both OrderNotFoundException + IllegalArgumentException → GlobalExceptionHandler
    }

    // DELETE /orders/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Integer id) {
        orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }

    // PATCH /orders/{id}/shipment
    @PatchMapping("/{id}/shipment")
    public ResponseEntity<OrderResponse> linkShipment(
            @PathVariable Integer id,
            @RequestBody @jakarta.validation.Valid OrderShipmentUpdateDTO dto) {
        return ResponseEntity.ok(orderService.linkShipment(id, dto.getShipmentId()));
    }
}
