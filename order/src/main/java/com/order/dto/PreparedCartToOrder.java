package com.order.dto;

import java.math.BigDecimal;

public record PreparedCartToOrder(Cart cart , BigDecimal price)  {
}
