package com.order.dto;
import com.order.DeliveryAddress;
import java.math.BigDecimal;

public record PreparedCartToOrder(Cart cart , DeliveryAddress deliveryAddress, BigDecimal price)  {
}
