package com.user;

import com.user.dto.CreatedUserDto;
import com.user.dto.UserRegistrationDto;
import com.user.dto.UserResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public interface UserMapper {
    UserMapper userMapper = Mappers.getMapper(UserMapper.class);

    UserResponseDto entityToDto(User user);

    CreatedUserDto createdEntityToDto(User user);


}
