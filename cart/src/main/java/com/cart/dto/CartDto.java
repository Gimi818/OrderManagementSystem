package com.cart.dto;

import java.util.Map;

public record CartDto (Map<Long, Integer> items){
}
