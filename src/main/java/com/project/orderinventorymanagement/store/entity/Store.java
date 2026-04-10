package com.project.orderinventorymanagement.store.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "stores")
public class Store {

    @Id
    @Column(name = "store_id")
    private Integer storeId;

    @Column(name = "store_name")
    private String storeName;

    @Column(name = "web_address")
    private String webAddress;

    @Column(name = "physical_address")
    private String physicalAddress;

    @Column(name = "latitude", precision = 9, scale = 6)
    private BigDecimal latitude;

    @Column(name = "longitude", precision = 9, scale = 6)
    private BigDecimal longitude;

    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Inventory> inventories;
   

    @Column(name = "logo_mime_type")
    private String logoMimeType;

    @Column(name = "logo_filename")
    private String logoFilename;

    @Column(name = "logo_charset")
    private String logoCharset;

    @Column(name = "logo_last_updated")
    private LocalDate logoLastUpdated;

	public Integer getStoreId() {
		return storeId;
	}

	public void setStoreId(Integer storeId) {
		this.storeId = storeId;
	}

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public String getWebAddress() {
		return webAddress;
	}

	public void setWebAddress(String webAddress) {
		this.webAddress = webAddress;
	}

	public String getPhysicalAddress() {
		return physicalAddress;
	}

	public void setPhysicalAddress(String physicalAddress) {
		this.physicalAddress = physicalAddress;
	}

	public BigDecimal getLatitude() {
		return latitude;
	}

	public void setLatitude(BigDecimal latitude) {
		this.latitude = latitude;
	}

	public BigDecimal getLongitude() {
		return longitude;
	}

	public void setLongitude(BigDecimal longitude) {
		this.longitude = longitude;
	}

	 

	public String getLogoMimeType() {
		return logoMimeType;
	}

	public void setLogoMimeType(String logoMimeType) {
		this.logoMimeType = logoMimeType;
	}

	public String getLogoFilename() {
		return logoFilename;
	}

	public void setLogoFilename(String logoFilename) {
		this.logoFilename = logoFilename;
	}

	public String getLogoCharset() {
		return logoCharset;
	}

	public void setLogoCharset(String logoCharset) {
		this.logoCharset = logoCharset;
	}

	public LocalDate getLogoLastUpdated() {
		return logoLastUpdated;
	}

	public void setLogoLastUpdated(LocalDate logoLastUpdated) {
		this.logoLastUpdated = logoLastUpdated;
	}

    // getters & setters
    
}