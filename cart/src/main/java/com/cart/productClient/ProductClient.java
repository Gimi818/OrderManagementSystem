package com.cart.productClient;

import com.cart.dto.ProductResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "product-service", url = "${application.config.products-url}")
public interface ProductClient {
    @GetMapping("/{id}")
    ResponseEntity<ProductResponseDto> findProductById(@PathVariable("id") Long id);
}
