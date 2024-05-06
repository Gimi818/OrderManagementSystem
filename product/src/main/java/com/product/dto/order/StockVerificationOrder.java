package com.product.dto.order;

import java.util.Set;

public record StockVerificationOrder(Long id, Set<OrderItem> items) {
}
