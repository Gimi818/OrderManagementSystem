package com.product;

import com.product.dto.*;
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

    @PostMapping(ProductController.Routes.ROOT)
    @ResponseStatus(HttpStatus.CREATED)
    public AddedProductDto addProduct(@RequestBody ProductRequestDto productRequestDto) {
        return service.addProduct(productRequestDto);
    }

    @GetMapping(FIND_ALL)
    public ResponseEntity<List<ProductResponseDto>> findAllProducts() {
        List<ProductResponseDto> allProducts = service.findAllProducts();
        return ResponseEntity.status(HttpStatus.OK).body(allProducts);
    }

    @PatchMapping(UPDATE_STOCK_QUANTITY)
    public ResponseEntity<ProductUpdateStockDto> updateStockQuantity(
            @PathVariable("id") Long id,
            @RequestBody StockQuantityDto stockQuantityDto) {
        ProductUpdateStockDto updatedProduct = service.updateStockQuantity(id, stockQuantityDto.stockQuantity());
        return ResponseEntity.ok(updatedProduct);
    }

    @PatchMapping(UPDATE_PRICE)
    public ResponseEntity<ProductUpdatePriceDto> updateProductPrice(
            @PathVariable ("id")Long id,
            @RequestBody PriceDto newPrice) {
        ProductUpdatePriceDto updatedProduct = service.updatePrice(id, newPrice.price());
        return ResponseEntity.ok(updatedProduct);
    }

    @GetMapping(FIND_BY_SORTED_ASC)
    public ResponseEntity<List<ProductResponseDto>> findAllProductsSortedByPriceAsc() {
        List<ProductResponseDto> allProducts = service.findAllProductsSortedByPriceAsc();
        return ResponseEntity.status(HttpStatus.OK).body(allProducts);
    }

    @GetMapping(FIND_BY_SORTED_DESC)
    public ResponseEntity<List<ProductResponseDto>> findAllProductsSortedByPriceDesc() {
        List<ProductResponseDto> allProducts = service.findAllProductsSortedByPriceDesc();
        return ResponseEntity.status(HttpStatus.OK).body(allProducts);
    }

    @GetMapping(FIND_BY_ID)
    public ResponseEntity<ProductResponseDto> findProductById(@PathVariable Long id) {
        ProductResponseDto product = service.findProductById(id);
        return ResponseEntity.status(HttpStatus.OK).body(product);
    }

    @GetMapping(FIND_BY_CATEGORY)
    public ResponseEntity<List<ProductResponseDto>> getProductsByCategory(@RequestParam("category") ProductCategory productCategory) {
        List<ProductResponseDto> products = service.findProductsByCategory(productCategory);
        return ResponseEntity.status(HttpStatus.OK).body(products);
    }

    @GetMapping(FIND_BY_PRICE_RANGE)
    public ResponseEntity<List<ProductResponseDto>> getProductsByPriceRange(
            @RequestParam("priceMin") BigDecimal priceMin,
            @RequestParam("priceMax") BigDecimal priceMax) {
        List<ProductResponseDto> products = service.findProductsByPriceRange(priceMin, priceMax);
        return ResponseEntity.status(HttpStatus.OK).body(products);
    }

    @DeleteMapping(DELETE_BY_ID)
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        service.deleteProduct(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    static final class Routes {
        static final String ROOT = "/api/v1/products";
        static final String DELETE_BY_ID = ROOT + "/{id}";
        static final String FIND_BY_ID = ROOT + "/{id}";
        static final String FIND_ALL = ROOT + "/all";
        static final String FIND_BY_CATEGORY = ROOT + "/category";
        static final String FIND_BY_SORTED_ASC = ROOT + "/sorted/price/asc";
        static final String FIND_BY_SORTED_DESC = ROOT + "/sorted/price/desc";
        static final String FIND_BY_PRICE_RANGE = ROOT + "/search/range/by-price";
        static final String UPDATE_STOCK_QUANTITY = ROOT + "/stock/{id}";
        static final String UPDATE_PRICE = ROOT + "/price/{id}";

    }


}
