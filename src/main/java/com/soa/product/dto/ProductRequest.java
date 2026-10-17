package com.soa.product.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

/**
 * Request payload for creating or updating a product.
 */
public class ProductRequest {

    @Schema(description = "Tên sản phẩm", example = "Tai nghe Sony WH-1000XM5")
    private String name;

    @Schema(description = "Mô tả chi tiết sản phẩm", example = "Tai nghe chống ồn chủ động cao cấp, pin 30 giờ")
    private String description;

    @Schema(description = "Giá sản phẩm", example = "6990000.00")
    private BigDecimal price;

    @Schema(description = "Số lượng sản phẩm trong kho", example = "15")
    private Integer quantity;

    public ProductRequest() {
    }

    public ProductRequest(String name, String description, BigDecimal price, Integer quantity) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
