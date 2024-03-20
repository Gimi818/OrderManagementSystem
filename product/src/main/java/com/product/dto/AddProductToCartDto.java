package com.product.dto;

import jakarta.validation.constraints.NotNull;

public record AddProductToCartDto(@NotNull Long userId, @NotNull Long productId, @NotNull int stockQuantity) {
}
