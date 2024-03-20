package com.cart.dto;

public record AddProductDto(Long userId, Long productId, int quantity) {
}
