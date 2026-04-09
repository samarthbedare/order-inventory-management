package com.project.orderinventorymanagement.productservice.dto;

import java.math.BigDecimal;

public class ProductUpdateDTO {

    private BigDecimal unitPrice;
    private Integer rating;
	public BigDecimal getUnitPrice() {
		return unitPrice;
	}
	public void setUnitPrice(BigDecimal unitPrice) {
		this.unitPrice = unitPrice;
	}
	public Integer getRating() {
		return rating;
	}
	public void setRating(Integer rating) {
		this.rating = rating;
	}

}

