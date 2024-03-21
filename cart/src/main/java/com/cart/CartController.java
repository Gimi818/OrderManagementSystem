package com.cart;

import com.cart.dto.CartDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import static com.cart.CartController.Routes.*;

@RestController
@RequiredArgsConstructor
public class CartController {
    private final CartService service;

    @GetMapping(GET_CART_CONTENTS)
    public ResponseEntity<CartDto> getCartContents(@PathVariable Long cartId) {
        CartDto cartDto = service.getCartContents(cartId);
        return new ResponseEntity<>(cartDto, HttpStatus.OK);
    }

    static final class Routes {
        static final String ROOT = "/api/v1/cart";
        static final String GET_CART_CONTENTS = ROOT + "/{cartId}/contents";

    }

}
