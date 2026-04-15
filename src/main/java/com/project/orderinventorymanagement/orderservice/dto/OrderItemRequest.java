package com.project.orderinventorymanagement.orderservice.dto;


import java.math.BigDecimal;

public class OrderItemRequest {

    private Integer productId;
    private BigDecimal unitPrice;
    private Integer quantity;

    public OrderItemRequest() {
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
