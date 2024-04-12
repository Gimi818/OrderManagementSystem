package com.cart;

import com.cart.dto.*;
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

                    for (CartItem item : cart.getItems()) {
                        Long productId = item.getProductId();
                        Integer quantity = item.getQuantity();

                        ProductInfo productInfo = getProductInfo(productId);

                        itemList.add(new ProductItem(productId, productInfo.name(), quantity));
                    }
                    BigDecimal totalPrice = calculateTotalCartPrice(cart);
                    return new CartDto(itemList, totalPrice);
                })
                .orElseThrow(() -> new NotFoundException("Cart not found with ID: " + cartId));
    }

    public BigDecimal calculateTotalCartPrice(Cart cart) {
        BigDecimal totalPrice = BigDecimal.ZERO;
        for (CartItem item : cart.getItems()) {
            Long productId = item.getProductId();
            Integer quantity = item.getQuantity();

            ProductInfo productInfo = getProductInfo(productId);
            BigDecimal itemTotalPrice = productInfo.price().multiply(BigDecimal.valueOf(quantity));
            totalPrice = totalPrice.add(itemTotalPrice);
        }
        return totalPrice;
    }
}
