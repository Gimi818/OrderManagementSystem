package com.cart;

import com.cart.dto.CartDto;
import com.cart.dto.ProductInfo;
import com.cart.dto.ProductItem;
import com.cart.dto.ProductResponseDto;
import com.cart.exception.exceptions.NotFoundException;
import com.cart.productClient.ProductClient;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.cart.CartService.ErrorMessages.NOT_FOUND_BY_ID;
import static com.cart.CartService.ErrorMessages.PRODUCT_NOT_FOUND;

@Service
@AllArgsConstructor
@Log4j2
public class CartRetrievalService {

    private final CartRepository repository;
    private final ProductClient productClient;

    public ProductInfo getProductInfo(Long productId) {
        ResponseEntity<ProductResponseDto> response = productClient.findProductById(productId);
        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            ProductResponseDto product = response.getBody();
            return new ProductInfo(productId, product.name(), product.price());
        }
        throw new NotFoundException(PRODUCT_NOT_FOUND + productId);
    }

    public CartDto getCartContents(Long cartId) {
        return repository.findById(cartId)
                .map(cart -> {
                    List<ProductItem> itemList = new ArrayList<>();
                    BigDecimal totalPrice = BigDecimal.ZERO;

                    for (Map.Entry<Long, Integer> entry : cart.getItems().entrySet()) {
                        Long productId = entry.getKey();
                        Integer quantity = entry.getValue();

                        ProductInfo productInfo = getProductInfo(productId);
                        BigDecimal itemTotalPrice = productInfo.price().multiply(BigDecimal.valueOf(quantity));
                        totalPrice = totalPrice.add(itemTotalPrice);

                        itemList.add(new ProductItem(productId, productInfo.name(), quantity));
                    }

                    return new CartDto(itemList, totalPrice);
                })
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_BY_ID, cartId));
    }

}
