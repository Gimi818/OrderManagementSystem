package com.product;

import com.product.dto.product.AddedProductDto;
import com.product.dto.product.ProductRequestDto;
import com.product.dto.product.ProductResponseDto;
import com.product.exception.exceptions.NotFoundException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@SpringBootTest
class ProductServiceTest {
    @Mock
    private ProductRepository productRepository;
    @Mock
    private ProductMapper productMapper;
    @InjectMocks
    private ProductService productService;
    @Mock
    private Product product;
    @Mock
    private Product secondProduct;
    @Mock
    private ProductResponseDto secondResponseProduct;
    @Mock
    private ProductRequestDto productRequestDto;
    @Mock
    private ProductResponseDto productResponseDto;
    @Mock
    private AddedProductDto addedProductDto;
    private ProductCategory productCategory;


    @BeforeEach
    void setUp() {
        productRequestDto = new ProductRequestDto("Samsung", new BigDecimal("3000.00"), 22, ProductCategory.TV);
        product = new Product(1L, "Samsung", new BigDecimal("3000.00"), 22, ProductCategory.TV);
        addedProductDto = new AddedProductDto("Samsung", 22);
        secondProduct = new Product(2L, "SONY", new BigDecimal("3590.00"), 32, ProductCategory.TV);
    }

    @Test
    @DisplayName("Should save product")
    void should_save_product() {
        given(productRepository.save(productMapper.dtoToEntity(productRequestDto)))
                .willReturn(product);
        assertThat(productService.addProduct(productRequestDto)).isEqualTo(productMapper.productToAddedProductResponseDto(product));
    }

    @Test
    @DisplayName("Should find all products by category")
    void should_find_all_products_by_category() {
        productResponseDto = new ProductResponseDto(1L, "Samsung", new BigDecimal("3000.00"), ProductCategory.TV);
        List<Product> productList = List.of(product);
        List<ProductCategory> expectedProductCategoryList = List.of(productCategory);

        given(productRepository.findProductByProductCategory(ProductCategory.TV)).willReturn(productList);

        given(productMapper.productToResponseDto(product)).willReturn(productResponseDto);

        List<ProductResponseDto> actualProductList = productService.findProductsByCategory(ProductCategory.TV);

        Assertions.assertThat(expectedProductCategoryList).isEqualTo(actualProductList);
        Mockito.verify(productMapper, Mockito.times(1)).productToResponseDto(product);


    }

    @Test
    @DisplayName("Should find all products ")
    void should_find_all_products() {
        productResponseDto = new ProductResponseDto(1L, "Samsung", new BigDecimal("3000.00"), ProductCategory.PHONE);
        secondResponseProduct = new ProductResponseDto(2L, "SONY", new BigDecimal("3590.00"), ProductCategory.TV);

        List<Product> products = List.of(product, secondProduct);
        List<ProductResponseDto> expectedProductCategoryList = List.of(productResponseDto, secondResponseProduct);

        given(productRepository.findAll()).willReturn(products);

        given(productMapper.productToResponseDto(product)).willReturn(productResponseDto);
        given(productMapper.productToResponseDto(secondProduct)).willReturn(productResponseDto);

        List<ProductResponseDto> actualProductList = productService.findAllProducts();

        Assertions.assertThat(expectedProductCategoryList).isEqualTo(actualProductList);
        Mockito.verify(productMapper, Mockito.times(1)).productToResponseDto(product);


    }

    @Test
    @DisplayName("Should throw productNotFoundException when product id doesn't exist ")
    void should_throw_productNotFoundException() {
        // Given
        Long nonExistingProductId = 10L;

        when(productRepository.findById(nonExistingProductId)).thenReturn(Optional.empty());

        // Then
        assertThrows(NotFoundException.class, () -> productService.findProductById(nonExistingProductId));
    }

    @Test
    @DisplayName("Should find product with mapping by id")
    void should_find_product_by_id() {
        given(productRepository.findById(1L)).willReturn(Optional.of(product));
        given(productMapper.productToResponseDto(product))
                .willReturn(productResponseDto);

        assertThat(productService.findProductById(1L)).isEqualTo(productResponseDto);
    }


}
