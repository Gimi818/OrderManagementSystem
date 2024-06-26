package com.cart;

import com.cart.dto.DeliveryAddress;
import com.cart.dto.CartDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.cart.CartController.Routes.*;

@RestController
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;
    private final CartRetrievalService cartRetrievalService;

    @GetMapping(GET_CART_CONTENTS)
    public ResponseEntity<CartDto> getCartContents(@PathVariable Long cartId) {
        CartDto cartDto = cartRetrievalService.getCartContents(cartId);
        return new ResponseEntity<>(cartDto, HttpStatus.OK);
    }

    @DeleteMapping(DELETE_PRODUCT_FROM_CART)
    public ResponseEntity<Void> deleteProduct(@PathVariable Long cartId, @PathVariable Long productId) {
        cartService.removeProductFromCart(cartId, productId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
    @PostMapping(PLACE_ORDER)
    public ResponseEntity<String> initiateOrder(@PathVariable (name = "cartId")Long cartId, @RequestBody DeliveryAddress deliveryAddress) {
        cartService.initiateOrderProcess(cartId , deliveryAddress);
        return ResponseEntity.ok("Order initiation started for cart ID: " + cartId);
    }

    static final class Routes {
        static final String ROOT = "/api/v1/carts";
        static final String GET_CART_CONTENTS = ROOT + "/{cartId}/contents";
        static final String DELETE_PRODUCT_FROM_CART = ROOT + "/{cartId}/{productId}";
        static final String PLACE_ORDER = ROOT +"/order/{cartId}";
    }

}
