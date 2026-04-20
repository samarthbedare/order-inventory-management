package com.project.orderinventorymanagement.orderservice.controller;


import com.project.orderinventorymanagement.orderservice.dto.OrderRequest;
import com.project.orderinventorymanagement.orderservice.dto.OrderResponse;
import com.project.orderinventorymanagement.orderservice.dto.OrderShipmentUpdateDTO;
import com.project.orderinventorymanagement.orderservice.dto.OrderStatusUpdateDTO;
import com.project.orderinventorymanagement.orderservice.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<List<OrderResponse>> createOrder(@RequestBody OrderRequest request) {
        List<OrderResponse> response = orderService.createOrder(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<OrderResponse>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable Integer id) {
        return ResponseEntity.ok(orderService.getOrderById(id));
    }

    @GetMapping("/store/{storeId}")
    public ResponseEntity<List<OrderResponse>> getOrdersByStore(@PathVariable Integer storeId) {
        return ResponseEntity.ok(orderService.getOrdersByStore(storeId));
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<OrderResponse>> getOrdersByCustomer(@PathVariable Integer customerId) {
        List<OrderResponse> orders = orderService.getOrdersByCustomer(customerId);
        return ResponseEntity.ok(orders);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<OrderResponse> updateStatus(
            @PathVariable Integer id,
            @RequestBody @jakarta.validation.Valid OrderStatusUpdateDTO dto) {
        String newStatus = dto.getOrderStatus();
        return ResponseEntity.ok(orderService.updateStatus(id, newStatus));

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Integer id) {
        orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/shipment")
    public ResponseEntity<OrderResponse> linkShipment(
            @PathVariable Integer id,
            @RequestBody @jakarta.validation.Valid OrderShipmentUpdateDTO dto) {
        return ResponseEntity.ok(orderService.linkShipment(id, dto.getShipmentId()));
    }
}
