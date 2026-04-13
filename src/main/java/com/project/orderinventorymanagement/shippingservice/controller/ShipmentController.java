package com.project.orderinventorymanagement.shippingservice.controller;

import com.project.orderinventorymanagement.customerservice.exception.CustomerNotFoundException;
import com.project.orderinventorymanagement.customerservice.service.CustomerService;
import com.project.orderinventorymanagement.shippingservice.dto.ShipmentRequestDTO;
import com.project.orderinventorymanagement.shippingservice.dto.ShipmentResponseDTO;
import com.project.orderinventorymanagement.shippingservice.entity.Shipment;
import com.project.orderinventorymanagement.shippingservice.entity.ShipmentStatus;
import com.project.orderinventorymanagement.shippingservice.service.ShipmentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/shipments")
public class ShipmentController {

    private final ShipmentService shipmentService;
    private final CustomerService customerService;

    public ShipmentController(ShipmentService shipmentService, CustomerService customerService) {
        this.shipmentService = shipmentService;
        this.customerService = customerService;
    }

    @PostMapping
    public ResponseEntity<ShipmentResponseDTO> createShipment(@RequestBody ShipmentRequestDTO request) {
        Shipment shipment = new Shipment();
        shipment.setDeliveryAddress(request.getDeliveryAddress());
        shipment.setStoreId(request.getStoreId());
        shipment.setCustomer(customerService.getCustomer(request.getCustomerId()));

        return ResponseEntity.status(HttpStatus.CREATED).body(convertToResponseDTO(shipmentService.createShipment(shipment)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ShipmentResponseDTO> getShipment(@PathVariable Integer id) {
        return ResponseEntity.ok(convertToResponseDTO(shipmentService.getShipmentById(id)));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ShipmentResponseDTO> updateStatus(@PathVariable Integer id, @RequestBody ShipmentStatus status) {
        return ResponseEntity.ok(convertToResponseDTO(shipmentService.updateStatus(id, status)));
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<ShipmentResponseDTO>> getCustomerShipments(@PathVariable Integer customerId) {
        if (!customerService.validateCustomer(customerId)) {
            throw new CustomerNotFoundException("Customer not found for customer id: " + customerId);
        }

        List<ShipmentResponseDTO> responseList = shipmentService.getShipmentsByCustomer(customerId)
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(responseList);
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