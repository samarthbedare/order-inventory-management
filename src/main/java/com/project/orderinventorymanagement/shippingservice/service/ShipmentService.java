package com.project.orderinventorymanagement.shippingservice.service;

import com.project.orderinventorymanagement.customerservice.entity.Customer;
import com.project.orderinventorymanagement.customerservice.exception.CustomerNotFoundException;
import com.project.orderinventorymanagement.customerservice.repository.CustomerRepository;
import com.project.orderinventorymanagement.shippingservice.dto.ShipmentRequestDTO;
import com.project.orderinventorymanagement.shippingservice.dto.ShipmentResponseDTO;
import com.project.orderinventorymanagement.shippingservice.entity.Shipment;
import com.project.orderinventorymanagement.shippingservice.entity.ShipmentStatus;
import com.project.orderinventorymanagement.shippingservice.exception.ShipmentNotFoundException;
import com.project.orderinventorymanagement.shippingservice.repository.ShipmentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ShipmentService {

    private final ShipmentRepository shipmentRepository;
    private final CustomerRepository customerRepository;

    public ShipmentService(ShipmentRepository shipmentRepository, CustomerRepository customerRepository) {
        this.shipmentRepository = shipmentRepository;
        this.customerRepository = customerRepository;
    }

    public ShipmentResponseDTO createShipment(ShipmentRequestDTO request) {
        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found for customer id: " + request.getCustomerId()));

        Shipment shipment = new Shipment();
        shipment.setDeliveryAddress(request.getDeliveryAddress());
        shipment.setStoreId(request.getStoreId());
        shipment.setCustomer(customer);
        shipment.setShipmentStatus(ShipmentStatus.CREATED);

        return convertToResponseDTO(shipmentRepository.save(shipment));
    }

    // This method kept for internal calls (like OrderService)
    public Shipment createShipment(Shipment shipment) {
        shipment.setShipmentStatus(ShipmentStatus.CREATED);
        return shipmentRepository.save(shipment);
    }

    public ShipmentResponseDTO getShipmentById(Integer id) {
        Shipment shipment = shipmentRepository.findById(id)
                .orElseThrow(() -> new ShipmentNotFoundException("Shipment not found for shipment id: " + id));
        return convertToResponseDTO(shipment);
    }

    public ShipmentResponseDTO updateStatus(Integer id, ShipmentStatus status) {
        Shipment shipment = shipmentRepository.findById(id)
                .orElseThrow(() -> new ShipmentNotFoundException("Shipment not found for shipment id: " + id));
        shipment.setShipmentStatus(status);
        return convertToResponseDTO(shipmentRepository.save(shipment));
    }

    public List<ShipmentResponseDTO> getShipmentsByCustomer(Integer customerId) {
         if (!customerRepository.existsById(customerId)) {
            throw new CustomerNotFoundException("Customer not found for customer id: " + customerId);
        }
        List<Shipment> shipments = shipmentRepository.findByCustomerCustomerId(customerId);
        if (shipments.isEmpty()) {
            throw new ShipmentNotFoundException("Shipments not found for customer id: " + customerId);
        }
        return shipments.stream().map(this::convertToResponseDTO).collect(Collectors.toList());
    }

    public List<ShipmentResponseDTO> getAllShipments() {
        List<Shipment> shipments = shipmentRepository.findAll();
        if (shipments.isEmpty()) {
            throw new ShipmentNotFoundException("No shipments found in the system.");
        }
        return shipments.stream().map(this::convertToResponseDTO).collect(Collectors.toList());
    }

    public List<ShipmentResponseDTO> getShipmentsByStore(Integer storeId) {
        List<Shipment> shipments = shipmentRepository.findByStoreId(storeId);
        if (shipments.isEmpty()) {
            throw new ShipmentNotFoundException("No shipments found for store id: " + storeId);
        }
        return shipments.stream().map(this::convertToResponseDTO).collect(Collectors.toList());
    }

    public void deleteShipment(Integer id) {
        if (!shipmentRepository.existsById(id)) {
            throw new ShipmentNotFoundException("Cannot delete: Shipment not found with id: " + id);
        }
        shipmentRepository.deleteById(id);
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
