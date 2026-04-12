package com.project.orderinventorymanagement.shippingservice.controller;

import java.util.List;
import java.util.stream.Collectors; 
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
    public ResponseEntity<?> createShipment(@RequestBody ShipmentRequestDTO request) {
        if (!customerService.validateCustomer(request.getCustomerId())) {
            return ResponseEntity.ok("Customer not found");
        }

        Shipment shipment = new Shipment();
        shipment.setDeliveryAddress(request.getDeliveryAddress());
        shipment.setStoreId(request.getStoreId());
        
        Customer customer = customerService.getCustomer(request.getCustomerId());
        shipment.setCustomer(customer);

        Shipment savedShipment = shipmentService.createShipment(shipment);
        
        return ResponseEntity.ok(convertToResponseDTO(savedShipment));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getShipment(@PathVariable Integer id) {
        Shipment shipment = shipmentService.getShipmentById(id);
        
        if (shipment == null) {
            return ResponseEntity.ok("Shipment not found for shipment id: " + id);
        }
        
        
        return ResponseEntity.ok(convertToResponseDTO(shipment));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<?> updateStatus(@PathVariable Integer id, @RequestBody ShipmentStatus status) {
        Shipment updated = shipmentService.updateStatus(id, status);
        
        if (updated == null) {
            return ResponseEntity.ok("Shipment not found for shipment id: " + id);
        }
        
       
        return ResponseEntity.ok(convertToResponseDTO(updated));
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<?> getCustomerShipments(@PathVariable Integer customerId) {
        if (!customerService.validateCustomer(customerId)) {
            return ResponseEntity.ok("Customer not found for customer id :" + customerId);
        }

        List<Shipment> shipments = shipmentService.getShipmentsByCustomer(customerId);
        
        if (shipments.isEmpty()) {
            return ResponseEntity.ok("Shipments not found for customer id: " + customerId);
        }

        
        List<ShipmentResponseDTO> responseDTOs = shipments.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(responseDTOs);
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
}