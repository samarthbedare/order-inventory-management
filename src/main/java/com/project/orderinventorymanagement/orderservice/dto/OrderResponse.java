package com.project.orderinventorymanagement.orderservice.dto;


import com.project.orderinventorymanagement.orderservice.model.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class OrderResponse {

    private Integer orderId;
    private LocalDateTime orderTms;
    private Integer customerId;
    private OrderStatus orderStatus;
    private Integer storeId;
    private List<ItemDetail> items;

    public OrderResponse() {
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public LocalDateTime getOrderTms() {
        return orderTms;
    }

    public void setOrderTms(LocalDateTime orderTms) {
        this.orderTms = orderTms;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Integer getStoreId() {
        return storeId;
    }

    public void setStoreId(Integer storeId) {
        this.storeId = storeId;
    }

    public List<ItemDetail> getItems() {
        return items;
    }

    public void setItems(List<ItemDetail> items) {
        this.items = items;
    }

    public static class ItemDetail {
        private Integer orderId;
        private Integer lineItemId;
        private Integer productId;
        private BigDecimal unitPrice;
        private Integer quantity;
        private Integer shipmentId;

        public ItemDetail() {
        }

        public Integer getOrderId() {
            return orderId;
        }

        public void setOrderId(Integer orderId) {
            this.orderId = orderId;
        }

        public Integer getLineItemId() {
            return lineItemId;
        }

        public void setLineItemId(Integer lineItemId) {
            this.lineItemId = lineItemId;
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

        public Integer getShipmentId() {
            return shipmentId;
        }

        public void setShipmentId(Integer shipmentId) {
            this.shipmentId = shipmentId;
        }
    }
}