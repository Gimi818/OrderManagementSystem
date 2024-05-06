package com.product.dto.order;

public record OrderItem(
        Long productId,
        Integer quantity) {
}
