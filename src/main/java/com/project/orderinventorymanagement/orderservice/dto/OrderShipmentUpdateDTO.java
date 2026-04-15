package com.project.orderinventorymanagement.orderservice.dto;

import jakarta.validation.constraints.NotNull;

public class OrderShipmentUpdateDTO {

    @NotNull(message = "Shipment ID must not be null")
    private Integer shipmentId;

    public OrderShipmentUpdateDTO() {
    }

    public Integer getShipmentId() {
        return shipmentId;
    }

    public void setShipmentId(Integer shipmentId) {
        this.shipmentId = shipmentId;
    }
}
