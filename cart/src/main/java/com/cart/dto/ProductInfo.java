package com.cart.dto;

import java.math.BigDecimal;

public record ProductInfo(Long id, String name, BigDecimal price) {
}
