package com.product;

import com.product.dto.product.*;
import com.product.kafka.KafkaProducer;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import com.product.exception.exceptions.NotFoundException;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import static com.product.ProductService.ErrorMessages.*;

@Service
@AllArgsConstructor
@Log4j2
public class ProductService {
    private final ProductRepository repository;
    private final ProductMapper mapper;
    private final KafkaProducer kafkaProducer;

    @Transactional
    public AddedProductDto addProduct(ProductRequestDto productRequestDto) {
        log.debug("Attempting to add a new product: {}", productRequestDto.name());
        Product product = mapper.dtoToEntity(productRequestDto);
        product = repository.save(product);
        log.info("Added product {}, stock quantity = {}", productRequestDto.name(), productRequestDto.stockQuantity());
        return mapper.productToAddedProductResponseDto(product);
    }

    public Product findProductById(Long productId) {
        log.debug("Searching for product by ID: {}", productId);
        Product product = repository.findById(productId).orElseThrow(() -> new NotFoundException(NOT_FOUND_PRODUCT_BY_ID, productId));
        log.info("Product found by ID {}", productId);
        return product;
    }

    public void addProductToCart(Long userId, Long productId, int quantity) {
        log.info("Processing addProductToCart, userId: {}, productId: {}, quantity: {}", userId, productId, quantity);
        Product product = findProductById(productId);
        AddProductToCartDto addProductToCartDto = new AddProductToCartDto(userId, product.getId(), quantity);
        kafkaProducer.sendProductToCartMessage("productToCart", addProductToCartDto);
        log.info("Product added to cart and message sent, userId: {}, productId: {}", userId, productId);
    }

    @Transactional
    public ProductUpdateStockDto updateStockQuantity(Long productId, int newStockQuantity) {
        log.debug("Updating stock quantity for product with productId: {}", productId);
        Product product = findProductById(productId);
        product.setStockQuantity(newStockQuantity);
        repository.save(product);
        log.info("Stock quantity updated for product with productId: {}. New stock quantity: {}", productId, newStockQuantity);
        return mapper.productToProductUpdateStockDto(product);
    }

    @Transactional
    public ProductUpdatePriceDto updateProductPrice(Long productId, BigDecimal newPrice) {
        log.debug("Updating price for product with productId: {}", productId);
        Product product = findProductById(productId);
        product.setPrice(newPrice);
        repository.save(product);
        log.info("Price updated for product with productId: {}. New price: {}", productId, newPrice);
        return mapper.productToProductUpdatePriceDto(product);
    }

    @Transactional
    public void deleteProduct(Long productId) {
        log.debug("Attempting to delete product with productId: {}", productId);
        findProductById(productId);
        log.info("Product deletion successful for productId: {}", productId);
        repository.deleteById(productId);
    }

    public List<ProductResponseDto> findProductsByCategory(ProductCategory productCategory) {
        log.debug("Searching for products by category: {}", productCategory);
        List<Product> productList = repository.findProductByProductCategory(productCategory);
        log.info("Products found by category {}", productCategory);
        return productList.stream().map(mapper::productToResponseDto).collect(Collectors.toList());

    }

    public List<ProductResponseDto> findProductsByPriceRange(BigDecimal priceMin, BigDecimal priceMax) {
        log.debug("Searching for products with prices between {} and {}", priceMin, priceMax);
        List<Product> productList = repository.findProductsByPriceRange(priceMin, priceMax);
        log.info("Products found with prices between {} and {}", priceMin, priceMax);
        return productList.stream().map(mapper::productToResponseDto).collect(Collectors.toList());
    }


    public List<ProductResponseDto> findAllProductsSortedByPriceAsc() {
        log.debug("Retrieving all products sorted by price in ascending order");
        List<Product> productList = repository.findAllProductsSortedByPriceAsc();
        log.info("Returning all products sorted by price in ascending order");
        return productList.stream().map(mapper::productToResponseDto).collect(Collectors.toList());
    }

    public List<ProductResponseDto> findAllProductsSortedByPriceDesc() {
        log.debug("Retrieving all products sorted by price in descending order");
        List<Product> productList = repository.findAllProductsSortedByPriceDesc();
        log.info("Returning all products sorted by price in descending order");
        return productList.stream().map(mapper::productToResponseDto).collect(Collectors.toList());
    }


    public List<ProductResponseDto> findAllProducts() {
        log.debug("Attempting to retrieve all products");
        List<Product> products = repository.findAll();
        log.info("Find all products");
        return repository.findAll().stream().map(mapper::productToResponseDto)
                .collect(Collectors.toList());
    }

    public ProductResponseDto findById(Long productId) {
        Product product = findProductById(productId);
        return mapper.productToResponseDto(product);
    }

    static final class ErrorMessages {
        static final String NOT_FOUND_PRODUCT_BY_ID = "Product with id %d not found";

    }
}
