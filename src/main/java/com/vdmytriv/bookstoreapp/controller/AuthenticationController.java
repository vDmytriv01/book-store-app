package com.vdmytriv.bookstoreapp.controller;

import com.vdmytriv.bookstoreapp.dto.user.UserLoginRequestDto;
import com.vdmytriv.bookstoreapp.dto.user.UserLoginResponseDto;
import com.vdmytriv.bookstoreapp.dto.user.UserRegisterRequestDto;
import com.vdmytriv.bookstoreapp.dto.user.UserResponseDto;
import com.vdmytriv.bookstoreapp.service.user.AuthenticationService;
import com.vdmytriv.bookstoreapp.service.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication", description = "Endpoints for authentication")
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final UserService userService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/registration")
    @Operation(summary = "Register user", description = "Creates a new user account.")
    public UserResponseDto registration(@Valid @RequestBody UserRegisterRequestDto requestDto) {
        return userService.registration(requestDto);
    }

    @PostMapping("/login")
    @Operation(summary = "Authenticate user",
            description = "Authenticates a user by email and password and returns a JWT token.")
    public UserLoginResponseDto login(@Valid @RequestBody UserLoginRequestDto request) {
        return authenticationService.authenticate(request);
    }
}
