package com.order.dto;

public record CartItem(Long id,
                       Cart cart,
                       Long productId,
                       Integer quantity) {

}
