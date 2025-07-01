package com.ecommerce.order.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class ProductResponse {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;

    private String category;

    private Boolean active;

    private Integer stockQuantity;

    private String imageUrl;

}
