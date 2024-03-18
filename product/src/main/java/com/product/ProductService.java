package com.product;

import com.product.dto.NewProductDto;
import com.product.dto.ProductResponseDto;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Log4j2
public class ProductService {
    private final ProductRepository repository;
    private final ProductMapper mapper;

    @Transactional
    public ProductResponseDto addProduct(NewProductDto newProductDto) {
        Product product = repository.save(mapper.dtoToEntity(newProductDto));
        log.info("Added product {}, stock quantity = {}", newProductDto.name(), newProductDto.StockQuantity());
        return mapper.productToResponseDto(product);
    }

    public ProductResponseDto findProductById(Long id) {
        Product product = repository.findById(id).orElseThrow();
        log.info("Found product with ID {}", id);
        return mapper.productToResponseDto(product);
    }

    public List<ProductResponseDto> findAllProducts() {
        log.info("Find all products");
        return repository.findAll().stream().map(mapper::productToResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteProduct(Long id) {
        repository.findById(id).orElseThrow();
        log.info("Product with id {} deleted", id);
        repository.deleteById(id);
    }
}
