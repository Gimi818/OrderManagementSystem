package com.product.dto;

import com.product.ProductCategory;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ProductRequestDto(@NotNull String name,
                                @NotNull BigDecimal price,
                                @NotNull int stockQuantity,
                                @NotNull ProductCategory productCategory) {
}
