package com.vdmytriv.bookstoreapp.dto.book;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;

public record BookDtoWithoutCategoryIds(
        @Schema(description = "Unique identifier", example = "1")
        Long id,

        @Schema(description = "Title of the book", example = "Effective Java")
        String title,

        @Schema(description = "Author of the book", example = "Joshua Bloch")
        String author,

        @Schema(description = "ISBN number", example = "978-3-16-148410-0")
        String isbn,

        @Schema(description = "Price of the book", example = "49.99")
        @PositiveOrZero
        BigDecimal price,

        @Schema(description = "Book description", example = "A book about Java best practices.")
        String description,

        @Schema(description = "Cover image URL", example = "https://example.com/image.jpg")
        String coverImage
) {
}
