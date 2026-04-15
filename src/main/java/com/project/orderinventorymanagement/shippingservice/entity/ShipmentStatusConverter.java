package com.project.orderinventorymanagement.shippingservice.entity;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class ShipmentStatusConverter implements AttributeConverter<ShipmentStatus, String> {

    @Override
    public String convertToDatabaseColumn(ShipmentStatus status) {
        if (status == null) return null;
        return (status == ShipmentStatus.IN_TRANSIT) ? "IN-TRANSIT" : status.name();
    }

    @Override
    public ShipmentStatus convertToEntityAttribute(String dbData) {
        if (dbData == null) return null;

        return (dbData.equals("IN-TRANSIT")) ? ShipmentStatus.IN_TRANSIT : ShipmentStatus.valueOf(dbData);
    }
}
