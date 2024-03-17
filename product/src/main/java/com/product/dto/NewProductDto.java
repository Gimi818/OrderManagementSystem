package com.product.dto;

import com.product.ProductCategory;

import java.math.BigDecimal;

public record NewProductDto(String name,
                            BigDecimal price,
                            int StockQuantity,
                            ProductCategory productCategory) {
}
