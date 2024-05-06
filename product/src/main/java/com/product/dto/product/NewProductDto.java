package com.product.dto.product;

import com.product.ProductCategory;

import java.math.BigDecimal;

public record NewProductDto(String name,
                            BigDecimal price,
                            int stockQuantity,
                            ProductCategory productCategory) {
}
