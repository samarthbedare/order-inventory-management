package com.project.orderinventorymanagement.shippingservice.controller;

import com.project.orderinventorymanagement.shippingservice.dto.ShipmentRequestDTO;
import com.project.orderinventorymanagement.shippingservice.dto.ShipmentResponseDTO;
import com.project.orderinventorymanagement.shippingservice.entity.ShipmentStatus;
import com.project.orderinventorymanagement.shippingservice.service.ShipmentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/shipments")
public class ShipmentController {

    private final ShipmentService shipmentService;

    public ShipmentController(ShipmentService shipmentService) {
        this.shipmentService = shipmentService;
    }

    @PostMapping
    public ResponseEntity<ShipmentResponseDTO> createShipment(@RequestBody ShipmentRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(shipmentService.createShipment(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ShipmentResponseDTO> getShipment(@PathVariable Integer id) {
        return ResponseEntity.ok(shipmentService.getShipmentById(id));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ShipmentResponseDTO> updateStatus(@PathVariable Integer id, @RequestBody ShipmentStatus status) {
        return ResponseEntity.ok(shipmentService.updateStatus(id, status));
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<ShipmentResponseDTO>> getCustomerShipments(@PathVariable Integer customerId) {
        return ResponseEntity.ok(shipmentService.getShipmentsByCustomer(customerId));
    }

    @GetMapping
    public ResponseEntity<List<ShipmentResponseDTO>> getAllShipments() {
        return ResponseEntity.ok(shipmentService.getAllShipments());
    }

    @GetMapping("/store/{storeId}")
    public ResponseEntity<List<ShipmentResponseDTO>> getStoreShipments(@PathVariable Integer storeId) {
        return ResponseEntity.ok(shipmentService.getShipmentsByStore(storeId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteShipment(@PathVariable Integer id) {
        shipmentService.deleteShipment(id);
        return ResponseEntity.noContent().build();
    }
}
