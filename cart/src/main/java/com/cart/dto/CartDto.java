package com.cart.dto;

import java.math.BigDecimal;
import java.util.List;

public record CartDto ( List<ProductItem> items,
                        BigDecimal totalPrice){
}
