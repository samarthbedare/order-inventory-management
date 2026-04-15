package com.project.orderinventorymanagement.orderservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.OneToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.UniqueConstraint;
import java.math.BigDecimal;

@Entity
@Table(name = "order_items", uniqueConstraints = {@UniqueConstraint(columnNames={"product_id", "order_id"})})
public class OrderItem {

    @Id
    @Column(name = "order_id")
    private Integer orderId; 

    @OneToOne
    @MapsId
    @JoinColumn(name = "order_id")
    private Order order;

    @Column(name = "line_item_id", nullable = false)
    private Integer lineItemId;

    @Column(name = "product_id", nullable = false)
    private Integer productId;

    @Column(name = "unit_price", nullable = false)
    private BigDecimal unitPrice;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "shipment_id")
    private Integer shipmentId;

    public OrderItem() {
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
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
