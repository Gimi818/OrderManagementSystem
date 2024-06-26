package com.order.dto;
import com.order.DeliveryAddress;
import java.math.BigDecimal;

public record OrderEmail(Long userId, DeliveryAddress deliveryAddress, String orderName, BigDecimal totalPrice) {
}
