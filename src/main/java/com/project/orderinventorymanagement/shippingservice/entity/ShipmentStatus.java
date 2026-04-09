package com.project.orderinventorymanagement.shippingservice.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum ShipmentStatus {
    CREATED,
    SHIPPED,

    @JsonProperty("IN-TRANSIT")
    IN_TRANSIT,
    DELIVERED
}