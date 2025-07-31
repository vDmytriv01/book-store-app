package com.vdmytriv.bookstoreapp.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;

public record UserResponseDto(
        @Schema(description = "Unique identifier of the user", example = "1")
        Long id,

        @Schema(description = "User's email address", example = "john.doe@example.com")
        String email,

        @Schema(description = "User's first name", example = "John")
        String firstName,

        @Schema(description = "User's last name", example = "Doe")
        String lastName,

        @Schema(description = "User's shipping address", example = "123 Main St, City, Country")
        String shippingAddress
) {
}
