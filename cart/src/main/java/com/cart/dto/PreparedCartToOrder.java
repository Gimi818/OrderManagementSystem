package com.cart.dto;

import com.cart.Cart;
import java.math.BigDecimal;

public record PreparedCartToOrder(Cart cart ,DeliveryAddress deliveryAddress, BigDecimal price)  {
}
