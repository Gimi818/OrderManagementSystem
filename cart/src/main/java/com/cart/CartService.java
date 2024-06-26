package com.cart;

import com.cart.dto.*;
import com.cart.exception.exceptions.AlreadyExistException;
import com.cart.exception.exceptions.NotFoundException;

import com.cart.kafka.KafkaProducer;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

import org.springframework.stereotype.Service;

import static com.cart.CartService.ErrorMessages.*;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Optional;

@Service
@AllArgsConstructor
@Log4j2
public class CartService {
    private final CartRepository repository;
    private final KafkaProducer kafkaProducer;
    private final CartRetrievalService cartRetrievalService;

    public void initiateOrderProcess(Long id, DeliveryAddress deliveryAddress) {
        log.debug("Initiating order process for cart ID: {}", id);
        kafkaProducer.initiateOrder("order-initiated", prepareCartForOrder(id,deliveryAddress));
        log.info("Order process initiated for cart ID: {}", id);

    }

    public PreparedCartToOrder prepareCartForOrder(Long id, DeliveryAddress deliveryAddress) {
        Cart cart = findCartById(id);
        BigDecimal totalPrice = cartRetrievalService.calculateTotalCartPrice(cart);
        return new PreparedCartToOrder(cart, deliveryAddress ,totalPrice);
    }

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

        Optional<CartItem> existingItem = findCartItem(cart,newProduct.productId());

        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + newProduct.stockQuantity());
        } else {
            CartItem newItem = new CartItem();
            newItem.setCart(cart);
            newItem.setProductId(newProduct.productId());
            newItem.setQuantity(newProduct.stockQuantity());
            cart.getItems().add(newItem);
        }

        repository.save(cart);
        log.info("Product added to cart. Cart ID: {}, Product ID: {}, Quantity: {}", newProduct.userId(), newProduct.productId(), newProduct.stockQuantity());
    }


    @Transactional
    public void removeProductFromCart(Long cartId, Long productId) {
        log.debug("Attempting to remove product from cart. Cart ID: {}, Product ID: {}", cartId, productId);
        Cart cart = findCartById(cartId);

        Optional<CartItem> itemToRemove = findCartItem(cart,productId);

        if (itemToRemove.isPresent()) {
            cart.getItems().remove(itemToRemove.get());
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


    public Cart createEmptyCartForUser(UserDto user) {
        Cart cart = new Cart();
        cart.setId(user.id());
        cart.setItems(new HashSet<>());
        return cart;
    }

    public void verifyCartDoesNotExist(UserDto user) {
        if (repository.existsById(user.id())) {
            log.warn("Attempt to create a cart for user ID: {} failed. Cart already exists.", user.id());
            throw new AlreadyExistException(CART_IS_EXIST, user.id());
        }


    }
    private Optional<CartItem> findCartItem(Cart cart, Long productId) {
        return cart.getItems().stream()
                .filter(item -> item.getProductId().equals(productId))
                .findFirst();
    }
    static final class ErrorMessages {
        static final String NOT_FOUND_BY_ID = "Cart with id %d not found";
        static final String PRODUCT_NOT_FOUND = "Product with id %d not found";
        static final String CART_IS_EXIST = "Cart for user ID: %d already exists";
    }
}
