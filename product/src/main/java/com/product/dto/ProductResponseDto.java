package com.product.dto;

import com.product.ProductCategory;

import java.math.BigDecimal;

public record ProductResponseDto(String name,
                                 BigDecimal price,
                                 ProductCategory productCategory
) {
}
