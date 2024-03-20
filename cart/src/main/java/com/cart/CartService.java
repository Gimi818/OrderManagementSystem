package com.cart;

import com.cart.dto.AddProductDto;
import com.cart.dto.UserIdDto;
import com.cart.exception.exceptions.AlreadyExistException;
import com.cart.exception.exceptions.NotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import static com.cart.CartService.ErrorMessages.*;

import java.util.HashMap;
import java.util.Map;

@Service
@AllArgsConstructor
@Log4j2
public class CartService {
    private final CartRepository repository;

    public void createUserCart(UserIdDto userIdDto) {
        log.debug("Attempting to create a cart for user ID: {}", userIdDto.id());
        if (repository.existsById(userIdDto.id())) {
            log.warn("Attempt to create a cart for user ID: {} failed. Cart already exists.", userIdDto.id());
            throw new AlreadyExistException(CART_IS_EXIST, userIdDto.id());
        }
        repository.save(creteEmptyCart(userIdDto));
        log.info("Cart created for user ID: {}", userIdDto.id());
    }

    public Cart creteEmptyCart(UserIdDto userIdDto) {
        Cart cart = new Cart();
        cart.setId(userIdDto.id());
        cart.setItems(new HashMap<>());
        return cart;
    }

    public void addProductToCart(AddProductDto newProduct) {
        log.debug("Attempting to add product to cart. Cart ID: {}, Product id: {}", newProduct.userId(), newProduct.productId());
        Cart cart = repository.findById(newProduct.userId()).orElseThrow(() ->
                new NotFoundException(NOT_FOUND_BY_ID, newProduct.userId()));
        Map<Long, Integer> items = cart.getItems();
        items.merge(newProduct.productId(), newProduct.quantity(), Integer::sum);
        repository.save(cart);
        log.info("Product added to cart. Cart ID: {}, Product ID: {}, Quantity: {}", newProduct.userId(), newProduct.productId(), newProduct.quantity());
    }

    public void removeProductFromCart(Long cartId, Long productId) {
        log.debug("Attempting to remove product from cart. Cart ID: {}, Product ID: {}", cartId, productId);
        Cart cart = repository.findById(cartId).orElseThrow(() ->
                new NotFoundException(NOT_FOUND_BY_ID, cartId));

        Map<Long, Integer> items = cart.getItems();
        Integer removed = items.remove(productId);

        if (removed != null) {
            repository.save(cart);
            log.info("Product removed from cart. Cart ID: {}, Product ID: {}", cartId, productId);
        } else {
            log.warn("Attempt to remove non-existent product from cart. Cart ID: {}, Product ID: {}", cartId, productId);


        }
    }

    static final class ErrorMessages {
        static final String NOT_FOUND_BY_ID = "Cart with id %d not found";
        static final String CART_IS_EXIST = "Cart for user ID: %d already exists";
    }
}
