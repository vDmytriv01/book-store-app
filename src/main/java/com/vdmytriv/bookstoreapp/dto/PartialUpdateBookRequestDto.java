package com.vdmytriv.bookstoreapp.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;

public record PartialUpdateBookRequestDto(
        @Schema(description = "Title of the book", example = "Updated Title")
        String title,
        @Schema(description = "Author of the book", example = "New Author")
        String author,
        @Schema(description = "ISBN number", example = "978-3-16-148410-0")
        @Pattern(regexp = "^[0-9\\-]*$")
        String isbn,
        @Schema(description = "Price of the book", example = "39.99")
        @PositiveOrZero
        BigDecimal price,
        @Schema(description = "Description of the book", example = "Updated description.")
        String description,
        @Schema(description = "URL of the book cover image", example = "https://example.com/updated-cover.jpg")
        String coverImage
) {
}
