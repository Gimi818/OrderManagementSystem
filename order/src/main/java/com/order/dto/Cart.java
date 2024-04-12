package com.order.dto;
import java.util.Set;

public record Cart(
        Long id,
        Set<CartItem> items) {
}
