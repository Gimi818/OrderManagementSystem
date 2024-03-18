package com.product;

import com.product.dto.*;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Log4j2
public class ProductService {
    private final ProductRepository repository;
    private final ProductMapper mapper;

    @Transactional
    public AddedProductDto addProduct(ProductRequestDto productRequestDto) {
        log.debug("Attempting to add a new product: {}", productRequestDto.name());
        Product product = repository.save(mapper.dtoToEntity(productRequestDto));
        log.info("Added product {}, stock quantity = {}", productRequestDto.name(), productRequestDto.StockQuantity());
        log.debug("Product added successfully: {}", product);
        return mapper.productToAddedProductResponseDto(product);
    }

    @Transactional
    public ProductUpdateStockDto updateStockQuantity(Long id, int newStockQuantity) {
        log.debug("Updating stock quantity for product with id: {}", id);
        Product product = repository.findById(id).orElseThrow();
        product.setStockQuantity(newStockQuantity);
        repository.save(product);
        log.info("Stock quantity updated for product with id: {}. New stock quantity: {}", id, newStockQuantity);
        return mapper.productToProductUpdateStockDto(product);
    }

    @Transactional
    public ProductUpdatePriceDto updatePrice(Long id, BigDecimal newPrice) {
        log.debug("Updating price for product with id: {}", id);
        Product product = repository.findById(id).orElseThrow();
        product.setPrice(newPrice);
        repository.save(product);
        log.info("Price updated for product with id: {}. New price: {}", id, newPrice);
        return mapper.productToProductUpdatePriceDto(product);
    }

    @Transactional
    public void deleteProduct(Long id) {
        log.debug("Attempting to delete product with id: {}", id);
        repository.findById(id).orElseThrow();
        log.info("Product with id {} deleted", id);
        log.debug("Product deletion successful for id: {}", id);
        repository.deleteById(id);
    }

    public List<ProductResponseDto> findProductsByCategory(ProductCategory productCategory) {
        log.debug("Searching for products by category: {}", productCategory);
        List<Product> productList = repository.findProductByProductCategory(productCategory);
        log.info("Found {} products", productList.size());
        log.debug("Products found by category {}: {}", productCategory, productList);
        return productList.stream().map(mapper::productToResponseDto).collect(Collectors.toList());

    }

    public List<ProductResponseDto> findProductsByPriceRange(BigDecimal priceMin, BigDecimal priceMax) {
        log.debug("Searching for products with prices between {} and {}", priceMin, priceMax);
        List<Product> productList = repository.findProductsByPriceRange(priceMin, priceMax);
        log.info("Found {} products", productList.size());
        log.debug("Products found with prices between {} and {}: {}", priceMin, priceMax, productList);
        return productList.stream().map(mapper::productToResponseDto).collect(Collectors.toList());
    }


    public List<ProductResponseDto> findAllProductsSortedByPriceAsc() {
        log.debug("Retrieving all products sorted by price in ascending order");
        List<Product> productList = repository.findAllProductsSortedByPriceAsc();
        log.info("Returning all products sorted by price in ascending order");
        log.debug("Products retrieved sorted by ascending price: {}", productList);
        return productList.stream().map(mapper::productToResponseDto).collect(Collectors.toList());
    }

    public List<ProductResponseDto> findAllProductsSortedByPriceDesc() {
        log.debug("Retrieving all products sorted by price in descending order");
        List<Product> productList = repository.findAllProductsSortedByPriceDesc();
        log.info("Returning all products sorted by price in in descending order");
        log.debug("Products retrieved sorted by descending price: {}", productList);
        return productList.stream().map(mapper::productToResponseDto).collect(Collectors.toList());
    }


    public ProductResponseDto findProductById(Long id) {
        log.debug("Searching for product by ID: {}", id);
        Product product = repository.findById(id).orElseThrow();
        log.info("Product found by ID {}", id);
        log.debug("Product found by ID {}: {}", id, product);
        return mapper.productToResponseDto(product);
    }

    public List<ProductResponseDto> findAllProducts() {
        log.debug("Attempting to retrieve all products");
        List<Product> products = repository.findAll();
        log.info("Find all products");
        log.debug("All products retrieved: {}", products);
        return repository.findAll().stream().map(mapper::productToResponseDto)
                .collect(Collectors.toList());
    }


}
