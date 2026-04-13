package com.project.orderinventorymanagement.shippingservice.service;

import com.project.orderinventorymanagement.shippingservice.entity.Shipment;
import com.project.orderinventorymanagement.shippingservice.entity.ShipmentStatus;
import com.project.orderinventorymanagement.shippingservice.exception.ShipmentNotFoundException;
import com.project.orderinventorymanagement.shippingservice.repository.ShipmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShipmentService {

    @Autowired
    private ShipmentRepository shipmentRepository;

    public Shipment createShipment(Shipment shipment) {
        shipment.setShipmentStatus(ShipmentStatus.CREATED);
        return shipmentRepository.save(shipment);
    }

    public Shipment getShipmentById(Integer id) {
        return shipmentRepository.findById(id)
                .orElseThrow(() -> new ShipmentNotFoundException("Shipment not found for shipment id: " + id));
    }

    public Shipment updateStatus(Integer id, ShipmentStatus status) {
        Shipment shipment = getShipmentById(id);
        shipment.setShipmentStatus(status);
        return shipmentRepository.save(shipment);
    }

    public List<Shipment> getShipmentsByCustomer(Integer customerId) {
        List<Shipment> shipments = shipmentRepository.findByCustomerCustomerId(customerId);
        if (shipments.isEmpty()) {
            throw new ShipmentNotFoundException("Shipments not found for customer id: " + customerId);
        }
        return shipments;
    }
}