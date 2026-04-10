package com.project.orderinventorymanagement.shippingservice.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.orderinventorymanagement.shippingservice.entity.Shipment;
import com.project.orderinventorymanagement.shippingservice.entity.ShipmentStatus;
import com.project.orderinventorymanagement.shippingservice.repository.ShipmentRepository;

@Service
public class ShipmentService {

    @Autowired
    private ShipmentRepository shipmentRepository;

    public Shipment createShipment(Shipment shipment) {
        shipment.setShipmentStatus(ShipmentStatus.CREATED.name()); 
        return shipmentRepository.save(shipment);
    }

    public Shipment getShipmentById(Integer id) { 
        return shipmentRepository.findById(id).orElse(null);
    }

    public Shipment updateStatus(Integer id, ShipmentStatus status) { 
        Shipment shipment = shipmentRepository.findById(id).orElse(null);
        if (shipment != null) {
            shipment.setShipmentStatus(status.name()); 
            return shipmentRepository.save(shipment);
        }
        return null;
    }

    public List<Shipment> getShipmentsByCustomer(Integer customerId) { 
        return shipmentRepository.findByCustomerCustomerId(customerId);
    }
}