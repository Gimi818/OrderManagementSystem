package com.order.dto;


import java.util.Set;

public record StockVerificationOrder(Long id, Set<OrderItemDto> items) {
}
