package com.vdmytriv.bookstoreapp.service.user;

import com.vdmytriv.bookstoreapp.dto.user.UserRegisterRequestDto;
import com.vdmytriv.bookstoreapp.dto.user.UserResponseDto;
import com.vdmytriv.bookstoreapp.exception.RegistrationException;
import com.vdmytriv.bookstoreapp.mapper.UserMapper;
import com.vdmytriv.bookstoreapp.model.User;
import com.vdmytriv.bookstoreapp.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserResponseDto registration(UserRegisterRequestDto requestDto) {
        if (userRepository.existsByEmail(requestDto.email())) {
            throw new RegistrationException("User with this email already exists");
        }
        User user = userMapper.toModel(requestDto);

        return userMapper.toDto(userRepository.save(user));
    }
}
