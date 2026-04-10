package com.project.orderinventorymanagement.shippingservice.service;

import com.project.orderinventorymanagement.shippingservice.entity.Shipment;
import com.project.orderinventorymanagement.shippingservice.entity.ShipmentStatus;
import com.project.orderinventorymanagement.shippingservice.repository.ShipmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShipmentService {

    @Autowired
    private ShipmentRepository shipmentRepository;

    private String toDbValue(ShipmentStatus status) {
        return status == ShipmentStatus.IN_TRANSIT ? "IN-TRANSIT" : status.name();
    }

    public Shipment createShipment(Shipment shipment) {
        shipment.setShipmentStatus(toDbValue(ShipmentStatus.CREATED));
        return shipmentRepository.save(shipment);
    }

    public Shipment getShipmentById(Integer id) {
        return shipmentRepository.findById(id).orElse(null);
    }

    public Shipment updateStatus(Integer id, ShipmentStatus status) {
        Shipment shipment = shipmentRepository.findById(id).orElse(null);
        if (shipment != null) {
            shipment.setShipmentStatus(toDbValue(status));
            return shipmentRepository.save(shipment);
        }
        return null;
    }

    public List<Shipment> getShipmentsByCustomer(Integer customerId) {
        return shipmentRepository.findByCustomerCustomerId(customerId);
    }
}