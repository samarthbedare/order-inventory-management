package com.project.orderinventorymanagement.orderservice.dto;

import jakarta.validation.constraints.NotBlank;

public class OrderStatusUpdateDTO {
    
    @NotBlank(message = "Order status must not be null or empty")
    private String orderStatus;

    public OrderStatusUpdateDTO() {
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }
}
