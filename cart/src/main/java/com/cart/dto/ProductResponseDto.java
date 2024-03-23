package com.cart.dto;

import java.math.BigDecimal;

public record ProductResponseDto(String name,
                                 BigDecimal price
                                 ) {
}
