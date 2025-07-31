package com.vdmytriv.bookstoreapp.dto.user;

import com.vdmytriv.bookstoreapp.validation.FieldMatch;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@FieldMatch(
        field = "password",
        fieldMatch = "repeatPassword",
        message = "Passwords do not match"
)
@Schema(description = "User registration request DTO")
public record UserRegisterRequestDto(

        @Schema(description = "User's email address",
                example = "john.doe@example.com")
        @NotBlank
        @Email(message = "Invalid email format")
        String email,

        @Schema(description = "User's password",
                example = "StrongPassword123")
        @NotBlank
        @Size(min = 6, max = 50)
        String password,

        @Schema(description = "Password confirmation",
                example = "StrongPassword123")
        @NotBlank
        String repeatPassword,

        @Schema(description = "User's first name",
                example = "John")
        @NotBlank
        String firstName,

        @Schema(description = "User's last name",
                example = "Doe")
        @NotBlank
        String lastName,

        @Schema(description = "User's shipping address",
                example = "123 Main St, City, Country")
        String shippingAddress
) {
}
