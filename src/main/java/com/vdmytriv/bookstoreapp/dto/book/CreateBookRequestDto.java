package com.vdmytriv.bookstoreapp.dto.book;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.util.List;

public record CreateBookRequestDto(
        @Schema(description = "Title of the book", example = "Effective Java")
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
        @NotNull
        @PositiveOrZero
        BigDecimal price,
        @Schema(description = "Description of the book",
                example = "A book about Java best practices.")
        @NotBlank
        String description,
        @Schema(description = "URL of the book cover image", example = "https://example.com/image.jpg")
        @NotBlank
        String coverImage,
        @Schema(description = "IDs of categories the book belongs to", example = "[1, 2]")
        List<Long> categoryIds
) {
}
