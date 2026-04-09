package com.project.orderinventorymanagement.shippingservice.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.orderinventorymanagement.shippingservice.dto.ShipmentRequestDTO;
import com.project.orderinventorymanagement.shippingservice.dto.ShipmentResponseDTO;
import com.project.orderinventorymanagement.shippingservice.entity.Shipment;
import com.project.orderinventorymanagement.shippingservice.entity.ShipmentStatus;
import com.project.orderinventorymanagement.shippingservice.service.ShipmentService;

@RestController
@RequestMapping("/api/shipments")
public class ShipmentController {

    @Autowired
    private ShipmentService shipmentService;

    @PostMapping
    public ResponseEntity<ShipmentResponseDTO> createShipment(@RequestBody ShipmentRequestDTO request) {

        Shipment shipment = new Shipment();
        shipment.setCustomerId(request.getCustomerId());
        shipment.setStoreId(request.getStoreId());
        shipment.setDeliveryAddress(request.getDeliveryAddress());

        Shipment savedShipment = shipmentService.createShipment(shipment);
        
        return ResponseEntity.ok(convertToResponseDTO(savedShipment));
    }

    private ShipmentResponseDTO convertToResponseDTO(Shipment shipment) {
        ShipmentResponseDTO dto = new ShipmentResponseDTO();
        dto.setShipmentId(shipment.getShipmentId());
        dto.setCustomerId(shipment.getCustomerId());
        dto.setStoreId(shipment.getStoreId());
        dto.setDeliveryAddress(shipment.getDeliveryAddress());
        dto.setShipmentStatus(shipment.getShipmentStatus());
        return dto;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Shipment> getShipment(@PathVariable Integer id) {
        Shipment shipment = shipmentService.getShipmentById(id);
        return shipment != null ? ResponseEntity.ok(shipment) : ResponseEntity.notFound().build();
    }

    @PatchMapping("/{id}/status")

    public ResponseEntity<Shipment> updateStatus(@PathVariable Integer id, @RequestBody ShipmentStatus status) {
        Shipment updated = shipmentService.updateStatus(id, status);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<Shipment>> getCustomerShipments(@PathVariable Integer customerId) {
        return ResponseEntity.ok(shipmentService.getShipmentsByCustomer(customerId));
    }
}
