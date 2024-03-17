package com.product;

import com.product.dto.NewProductDto;
import com.product.dto.ProductResponseDto;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public interface ProductMapper {

    Product dtoToEntity(NewProductDto newProductDto);
    ProductResponseDto productToResponseDto(Product product);

}
