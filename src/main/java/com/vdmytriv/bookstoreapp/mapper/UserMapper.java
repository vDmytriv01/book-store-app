package com.vdmytriv.bookstoreapp.mapper;

import com.vdmytriv.bookstoreapp.config.MapperConfig;
import com.vdmytriv.bookstoreapp.dto.user.UserRegisterRequestDto;
import com.vdmytriv.bookstoreapp.dto.user.UserResponseDto;
import com.vdmytriv.bookstoreapp.model.User;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface UserMapper {
    UserResponseDto toDto(User user);

    User toModel(UserRegisterRequestDto userRegisterRequestDto);
}
