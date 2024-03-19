package com.cart;

import com.cart.dto.AddProductDto;
import com.cart.dto.CartRequestDto;
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


    public void createUserCart(CartRequestDto cartRequestDto) {
        log.debug("Attempting to create a cart for user ID: {}", cartRequestDto.id());
        if (repository.existsById(cartRequestDto.id())) {
            log.warn("Attempt to create a cart for user ID: {} failed. Cart already exists.", cartRequestDto.id());
            throw new AlreadyExistException(CART_IS_EXIST, cartRequestDto.id());
        }
        Cart cart = new Cart();
        cart.setId(cartRequestDto.id());
        cart.setItems(new HashMap<>());
        repository.save(cart);
        log.info("Cart created for user ID: {}", cartRequestDto.id());
    }


    public void addProductToCart(Long id, AddProductDto newProduct) {
        log.debug("Attempting to add product to cart. Cart ID: {}, Product: {}", id, newProduct.productName());
        Cart cart = repository.findById(id).orElseThrow(() ->
                new NotFoundException(NOT_FOUND_BY_ID, id));
        Map<String, Integer> items = cart.getItems();
        items.merge(newProduct.productName(), newProduct.quantity(), Integer::sum);
        repository.save(cart);
        log.info("Product added to cart. Cart ID: {}, Product: {}, New Quantity: {}", id, newProduct.productName(), newProduct.quantity());
    }
    public void removeProductFromCart(Long id, String productName) {
        log.debug("Attempting to remove product from cart. Cart ID: {}, Product Name: {}", id, productName);
        Cart cart = repository.findById(id).orElseThrow(() ->
                new NotFoundException(NOT_FOUND_BY_ID, id));

        Map<String, Integer> items = cart.getItems();
        Integer removed = items.remove(productName);

        if (removed != null) {
            repository.save(cart);
            log.info("Product removed from cart. Cart ID: {}, Product Name: {}", id, productName);
        } else {
            log.warn("Attempt to remove non-existent product from cart. Cart ID: {}, Product Name: {}", id, productName);
       

        }
    }
    static final class ErrorMessages {
        static final String NOT_FOUND_BY_ID = "Cart with id %d not found";
        static final String CART_IS_EXIST = "Cart for user ID: %d already exists";
    }
}
