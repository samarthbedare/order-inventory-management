package com.project.orderinventorymanagement;

import com.project.orderinventorymanagement.orderservice.dto.OrderItemRequest;
import com.project.orderinventorymanagement.orderservice.dto.OrderRequest;
import com.project.orderinventorymanagement.orderservice.entity.Order;
import com.project.orderinventorymanagement.orderservice.entity.OrderItem;
import com.project.orderinventorymanagement.productservice.entity.Product;
import com.project.orderinventorymanagement.storeservice.entity.Inventory;
import com.project.orderinventorymanagement.storeservice.entity.Store;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class PojoTest {

    @Test
    void testProductEntity() {
        Product p = new Product();
        p.setProductId(1);
        p.setProductName("Name");
        p.setUnitPrice(BigDecimal.TEN);
        p.setBrand("Brand");
        p.setColour("Color");
        p.setSize("Size");
        p.setRating(5);
        p.setInventories(new ArrayList<>());

        assertThat(p.getProductId()).isEqualTo(1);
        assertThat(p.getProductName()).isEqualTo("Name");
        assertThat(p.getUnitPrice()).isEqualTo(BigDecimal.TEN);
        assertThat(p.getBrand()).isEqualTo("Brand");
        assertThat(p.getColour()).isEqualTo("Color");
        assertThat(p.getSize()).isEqualTo("Size");
        assertThat(p.getRating()).isEqualTo(5);
        assertThat(p.getInventories()).isEmpty();

        // Constructor test
        Product p2 = new Product(1, "Name", BigDecimal.TEN, "Color", "Brand", "Size", 5);
        assertThat(p2.getProductId()).isEqualTo(1);
    }

    @Test
    void testStoreEntity() {
        Store s = new Store();
        s.setStoreId(1);
        s.setStoreName("Store");
        s.setWebAddress("web");
        s.setPhysicalAddress("physical");
        s.setLatitude(BigDecimal.ONE);
        s.setLongitude(BigDecimal.ONE);
        s.setLogoMimeType("mime");
        s.setLogoFilename("file");
        s.setLogoCharset("charset");
        s.setLogoLastUpdated(LocalDate.now());

        assertThat(s.getStoreId()).isEqualTo(1);
        assertThat(s.getStoreName()).isEqualTo("Store");
        assertThat(s.getWebAddress()).isEqualTo("web");
        assertThat(s.getPhysicalAddress()).isEqualTo("physical");
        assertThat(s.getLatitude()).isEqualTo(BigDecimal.ONE);
        assertThat(s.getLongitude()).isEqualTo(BigDecimal.ONE);
        assertThat(s.getLogoMimeType()).isEqualTo("mime");
        assertThat(s.getLogoFilename()).isEqualTo("file");
        assertThat(s.getLogoCharset()).isEqualTo("charset");
        assertThat(s.getLogoLastUpdated()).isNotNull();
    }

    @Test
    void testInventoryEntity() {
        Inventory i = new Inventory();
        i.setStore(new Store());
        i.setProduct(new Product());
        i.setProductInventory(10);

        assertThat(i.getStore()).isNotNull();
        assertThat(i.getProduct()).isNotNull();
        assertThat(i.getProductInventory()).isEqualTo(10);
    }

    @Test
    void testOrderEntities() {
        Order o = new Order();
        o.setOrderTms(LocalDateTime.now());
        assertThat(o.getOrderTms()).isNotNull();

        OrderItem item = new OrderItem();
        item.setLineItemId(1);
        assertThat(item.getLineItemId()).isEqualTo(1);

        OrderRequest req = new OrderRequest();
        req.setCustomerId(1);
        req.setStoreId(1);
        req.setItems(List.of(new OrderItemRequest()));
        assertThat(req.getCustomerId()).isEqualTo(1);
        assertThat(req.getStoreId()).isEqualTo(1);
        assertThat(req.getItems()).hasSize(1);
    }
}
