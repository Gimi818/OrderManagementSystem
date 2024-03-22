package com.cart;

import com.cart.dto.AddProductDto;
import com.cart.dto.CartDto;
import com.cart.dto.UserDto;
import com.cart.exception.exceptions.AlreadyExistException;
import com.cart.exception.exceptions.NotFoundException;
import jakarta.transaction.Transactional;
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

    @Transactional
    public void createUserCart(UserDto user) {
        log.debug("Attempting to create a cart for user ID: {}", user.id());
        verifyCartDoesNotExist(user);
        repository.save(createEmptyCartForUser(user));
        log.info("Cart created for user ID: {}", user.id());
    }

    @Transactional
    public void addProductToCart(AddProductDto newProduct) {
        log.debug("Attempting to add product to cart. Cart ID: {}, Product id: {}", newProduct.userId(), newProduct.productId());
        Cart cart = findCartById(newProduct.userId());
        Map<Long, Integer> items = cart.getItems();
        items.merge(newProduct.productId(), newProduct.stockQuantity(), Integer::sum);
        repository.save(cart);
        log.info("Product added to cart. Cart ID: {}, Product ID: {}, Quantity: {}", newProduct.userId(), newProduct.productId(), newProduct.stockQuantity());
    }

    @Transactional
    public void removeProductFromCart(Long cartId, Long productId) {
        log.debug("Attempting to remove product from cart. Cart ID: {}, Product ID: {}", cartId, productId);
        Cart cart = findCartById(cartId);

        Map<Long, Integer> items = cart.getItems();
        Integer removed = items.remove(productId);

        if (removed != null) {
            repository.save(cart);
            log.info("Product removed from cart. Cart ID: {}, Product ID: {}", cartId, productId);
        } else {
            log.warn("Attempt to remove non-existent product from cart. Cart ID: {}, Product ID: {}", cartId, productId);

        }
    }

    public Cart findCartById(Long cartId) {
        log.debug("Attempting to find a cart with ID: {}", cartId);
        Cart cart = repository.findById(cartId).orElseThrow(() ->
                new NotFoundException(NOT_FOUND_BY_ID, cartId));
        log.info("Cart found with ID: {}", cartId);
        return cart;
    }

    public CartDto getCartContents(Long cartId) {
        return repository.findById(cartId)
                .map(cart -> new CartDto(cart.getItems()))
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_BY_ID, cartId));
    }

    public Cart createEmptyCartForUser(UserDto user) {
        Cart cart = new Cart();
        cart.setId(user.id());
        cart.setItems(new HashMap<>());
        return cart;
    }

    public void verifyCartDoesNotExist(UserDto user) {
        if (repository.existsById(user.id())) {
            log.warn("Attempt to create a cart for user ID: {} failed. Cart already exists.", user.id());
            throw new AlreadyExistException(CART_IS_EXIST, user.id());
        }
    }

    static final class ErrorMessages {
        static final String NOT_FOUND_BY_ID = "Cart with id %d not found";
        static final String CART_IS_EXIST = "Cart for user ID: %d already exists";
    }
}
