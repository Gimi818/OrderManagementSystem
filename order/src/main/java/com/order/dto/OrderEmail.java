package com.order.dto;

import java.math.BigDecimal;

public record OrderEmail(Long userId,String orderName, BigDecimal totalPrice) {
}
