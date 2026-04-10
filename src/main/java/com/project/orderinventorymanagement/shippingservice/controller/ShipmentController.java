package com.project.orderinventorymanagement.shippingservice.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.project.orderinventorymanagement.customerservice.entity.Customer;
import com.project.orderinventorymanagement.customerservice.service.CustomerService;
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

    @Autowired
    private CustomerService customerService;

    @PostMapping
    public ResponseEntity<ShipmentResponseDTO> createShipment(@RequestBody ShipmentRequestDTO request) {
        Shipment shipment = new Shipment();
        shipment.setDeliveryAddress(request.getDeliveryAddress());
        shipment.setStoreId(request.getStoreId());
        
        
        Customer customer = customerService.getCustomer(request.getCustomerId());
        
        shipment.setCustomer(customer);

        Shipment savedShipment = shipmentService.createShipment(shipment);
        return ResponseEntity.ok(convertToResponseDTO(savedShipment));
    }

    private ShipmentResponseDTO convertToResponseDTO(Shipment shipment) {
        ShipmentResponseDTO dto = new ShipmentResponseDTO();
        dto.setShipmentId(shipment.getShipmentId());
        dto.setCustomerId(shipment.getCustomer().getCustomerId());
        dto.setStoreId(shipment.getStoreId());
        dto.setDeliveryAddress(shipment.getDeliveryAddress());
        dto.setShipmentStatus(shipment.getShipmentStatus());
        return dto;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Shipment> getShipment(@PathVariable Long id) {
        Shipment shipment = shipmentService.getShipmentById(id);
        return shipment != null ? ResponseEntity.ok(shipment) : ResponseEntity.notFound().build();
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Shipment> updateStatus(@PathVariable Long id, @RequestBody ShipmentStatus status) {
        Shipment updated = shipmentService.updateStatus(id, status);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<Shipment>> getCustomerShipments(@PathVariable Integer customerId) {
        return ResponseEntity.ok(shipmentService.getShipmentsByCustomer(customerId));
    }
}