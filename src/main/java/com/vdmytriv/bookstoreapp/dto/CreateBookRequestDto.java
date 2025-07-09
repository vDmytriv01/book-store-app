package com.vdmytriv.bookstoreapp.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record CreateBookRequestDto(
        @NotBlank
        String title,
        @NotBlank
        String author,
        @NotBlank
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
