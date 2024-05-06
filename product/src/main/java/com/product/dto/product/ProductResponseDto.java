package com.product.dto.product;

import com.product.ProductCategory;

import java.math.BigDecimal;

public record ProductResponseDto(String name,
                                 BigDecimal price,
                                 ProductCategory productCategory
) {
}
