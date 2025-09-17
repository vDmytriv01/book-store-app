package com.vdmytriv.bookstoreapp.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserLoginRequestDto(
        @Schema(description = "User's email address", example = "john.doe@example.com")
        @NotBlank
        @Email(message = "Invalid email format")
        String email,

        @Schema(description = "User's password", example = "StrongPassword123")
        @NotBlank
        @Size(min = 6, max = 50)
        String password
) {
}
