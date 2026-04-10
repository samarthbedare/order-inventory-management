package com.project.orderinventorymanagement.productservice.entity;

import com.project.orderinventorymanagement.store.entity.Inventory;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Integer productId;

    @Column(name = "product_name", nullable = false)
    private String productName;

    @Column(name = "unit_price", nullable = false)
    private BigDecimal unitPrice;

    @Column(name = "colour", nullable = false)
    private String colour;

    @Column(name = "brand", nullable = false)
    private String brand;

    @Column(name = "size", nullable = false)
    private String size;

    @Column(name = "rating", nullable = false)
    private Integer rating;

    @OneToMany(mappedBy = "product")
    private List<Inventory> inventories;

    public Product() {
    }

    public Product(Integer productId, String productName, BigDecimal unitPrice,
                   String colour, String brand, String size, Integer rating) {
        this.productId = productId;
        this.productName = productName;
        this.unitPrice = unitPrice;
        this.colour = colour;
        this.brand = brand;
        this.size = size;
        this.rating = rating;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getColour() {
        return colour;
    }

    public void setColour(String colour) {
        this.colour = colour;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public List<Inventory> getInventories() {
        return inventories;
    }

    public void setInventories(List<Inventory> inventories) {
        this.inventories = inventories;
    }
}