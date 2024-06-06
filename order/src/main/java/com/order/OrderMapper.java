package com.order;

import com.order.dto.OrderEmail;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public interface OrderMapper {
    OrderMapper orderMapper = Mappers.getMapper(OrderMapper.class);

    OrderEmail entityToOrderEmail(Order order);
}
