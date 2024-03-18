package com.product;

import com.product.dto.AddedProductDto;
import com.product.dto.NewProductDto;
import com.product.dto.ProductRequestDto;
import com.product.dto.ProductResponseDto;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public interface ProductMapper {

    Product dtoToEntity(ProductRequestDto productRequestDto);

    ProductResponseDto productToResponseDto(Product product);

    AddedProductDto productToAddedProductResponseDto(Product product);

}
