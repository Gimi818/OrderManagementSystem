package com.order.dto;

public record CartItem(Long id,
                       Cart cart,
                       String productName,
                       Long productId,
                       Integer quantity) {

}
