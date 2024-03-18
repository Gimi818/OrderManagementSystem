package com.product;

import com.product.dto.AddedProductDto;
import com.product.dto.ProductRequestDto;
import com.product.dto.ProductResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import static com.product.ProductController.Routes.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ProductController {
    private final ProductService service;

    @PostMapping(ROOT)
    public ResponseEntity<AddedProductDto> addProduct(@RequestBody ProductRequestDto productRequestDto) {
        return new ResponseEntity<>(service.addProduct(productRequestDto), HttpStatus.CREATED);
    }

    @GetMapping(FIND_ALL)
    public ResponseEntity<List<ProductResponseDto>> findAllProducts() {
        List<ProductResponseDto> allProducts = service.findAllProducts();
        return ResponseEntity.status(HttpStatus.OK).body(allProducts);
    }

    @GetMapping( FIND_BY_ID)
    public ProductResponseDto findProductById(@PathVariable Long id) {
        return service.findProductById(id);
    }

    @GetMapping(FIND_BY_CATEGORY)
    public ResponseEntity<List<ProductResponseDto>> getProductsByCategory(@RequestParam("category") ProductCategory productCategory) {
        List<ProductResponseDto> products = service.findProductsByCategory(productCategory);
        return ResponseEntity.status(HttpStatus.OK).body(products);
    }

    @DeleteMapping(DELETE_BY_ID)
    public ResponseEntity<Void> deleteFilm(@PathVariable Long id) {
        service.deleteProduct(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    static final class Routes {
        static final String ROOT = "/api/v1/products";
        static final String DELETE_BY_ID = ROOT + "/{id}";
        static final String FIND_BY_ID = ROOT + "/{id}";
        static final String FIND_ALL = ROOT + "/all";
        static final String FIND_BY_CATEGORY = ROOT + "/category";

    }



}
