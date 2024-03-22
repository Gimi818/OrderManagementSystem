package com.product.dto;

import com.product.ProductCategory;

import java.math.BigDecimal;

public record ProductRequestDto(String name,
                                BigDecimal price,
                                int stockQuantity,
                                ProductCategory productCategory) {
}
