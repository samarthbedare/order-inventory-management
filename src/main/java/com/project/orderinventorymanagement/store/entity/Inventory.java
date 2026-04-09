package com.project.orderinventorymanagement.store.entity;

 
import jakarta.persistence.*;

@Entity
@Table(name = "inventory")
public class Inventory {

    @Id
    @Column(name = "inventory_id")
    private Integer inventoryId;

    @Column(name = "store_id")
    private Integer storeId;

    @Column(name = "product_id")
    private Integer productId;

    @Column(name = "product_inventory")
    private Integer productInventory; // quantity

	public Integer getInventoryId() {
		return inventoryId;
	}

	public void setInventoryId(Integer inventoryId) {
		this.inventoryId = inventoryId;
	}

	public Integer getStoreId() {
		return storeId;
	}

	public void setStoreId(Integer storeId) {
		this.storeId = storeId;
	}

	public Integer getProductId() {
		return productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
	}

	public Integer getProductInventory() {
		return productInventory;
	}

	public void setProductInventory(Integer productInventory) {
		this.productInventory = productInventory;
	}

    // getters & setters
    
    
}