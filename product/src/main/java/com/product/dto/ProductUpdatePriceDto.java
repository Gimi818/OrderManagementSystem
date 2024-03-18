package com.product.dto;

import java.math.BigDecimal;

public record ProductUpdatePriceDto(String name, BigDecimal price) {
}
