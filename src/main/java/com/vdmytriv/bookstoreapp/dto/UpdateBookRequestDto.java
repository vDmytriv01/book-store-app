package com.vdmytriv.bookstoreapp.dto;

import jakarta.validation.constraints.Min;
import java.math.BigDecimal;

public record UpdateBookRequestDto(
        String title,
        String author,
        String isbn,
        @Min(0)
        BigDecimal price,
        String description,
        String coverImage
) {
}
