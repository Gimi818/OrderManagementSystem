package com.emailsender.dto;

import java.math.BigDecimal;

public record OrderDto (Long userId, DeliveryAddress deliveryAddress ,String orderName, BigDecimal totalPrice){
}
