package com.product.dto.product;

import java.math.BigDecimal;

public record ProductUpdatePriceDto(String name, BigDecimal price) {
}
