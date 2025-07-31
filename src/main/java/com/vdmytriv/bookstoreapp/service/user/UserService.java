package com.vdmytriv.bookstoreapp.service.user;

import com.vdmytriv.bookstoreapp.dto.user.UserRegisterRequestDto;
import com.vdmytriv.bookstoreapp.dto.user.UserResponseDto;

public interface UserService {
    UserResponseDto registration(UserRegisterRequestDto request);
}
