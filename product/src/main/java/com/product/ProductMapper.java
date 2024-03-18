package com.product;

import com.product.dto.*;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public interface ProductMapper {

    Product dtoToEntity(ProductRequestDto productRequestDto);

    ProductResponseDto productToResponseDto(Product product);

    AddedProductDto productToAddedProductResponseDto(Product product);

    ProductUpdateStockDto productToProductUpdateStockDto(Product product);

    ProductUpdatePriceDto productToProductUpdatePriceDto(Product product);

}
