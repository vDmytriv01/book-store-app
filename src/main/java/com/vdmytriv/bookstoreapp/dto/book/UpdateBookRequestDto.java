package com.vdmytriv.bookstoreapp.dto.book;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;

public record UpdateBookRequestDto(
        @Schema(description = "Title of the book", example = "Updated Effective Java")
        @NotBlank
        String title,
        @Schema(description = "Author of the book", example = "Joshua Bloch")
        @NotBlank
        String author,
        @Schema(description = "ISBN number", example = "978-3-16-148410-0")
        @NotBlank
        @Pattern(regexp = "^[0-9\\-]*$")
        String isbn,
        @Schema(description = "Price of the book", example = "49.99")
        @PositiveOrZero
        BigDecimal price,
        @Schema(description = "Description of the book",
                example = "Updated description of the book.")
        @NotBlank
        String description,
        @Schema(description = "URL of the book cover image", example = "https://example.com/updated-cover.jpg")
        @NotBlank
        String coverImage
) {
}
