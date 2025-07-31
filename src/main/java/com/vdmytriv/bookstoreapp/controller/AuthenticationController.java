package com.vdmytriv.bookstoreapp.controller;

import com.vdmytriv.bookstoreapp.dto.user.UserRegisterRequestDto;
import com.vdmytriv.bookstoreapp.dto.user.UserResponseDto;
import com.vdmytriv.bookstoreapp.service.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth/")
public class AuthenticationController {

    private final UserService userService;

    @PostMapping("/registration")
    public UserResponseDto registration(@Valid @RequestBody UserRegisterRequestDto requestDto) {
        return userService.registration(requestDto);
    }
}
