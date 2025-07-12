package com.vdmytriv.bookstoreapp.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.math.BigDecimal;

public record CreateBookRequestDto(
        @NotBlank
        String title,
        @NotBlank
        String author,
        @NotBlank
        @Pattern(regexp = "^[0-9\\-]*$")
        String isbn,
        @Min(0)
        @NotNull
        BigDecimal price,
        @NotBlank
        String description,
        @NotBlank
        String coverImage
) {
}
