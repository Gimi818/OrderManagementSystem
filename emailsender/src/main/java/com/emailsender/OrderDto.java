package com.emailsender;

import java.math.BigDecimal;

public record OrderDto (Long userId, String orderName, BigDecimal totalPrice){
}
