package com.product;

import com.product.dto.NewProductDto;
import com.product.dto.ProductResponseDto;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Log4j2
public class ProductService {
    private final ProductRepository repository;
    private final ProductMapper mapper;


    public ProductResponseDto addProduct(NewProductDto newProductDto) {
        Product product = repository.save(mapper.dtoToEntity(newProductDto));
        log.info("Added product {}, stock quantity = {}", newProductDto.name(), newProductDto.StockQuantity());
        return mapper.productToResponseDto(product);
    }




}
