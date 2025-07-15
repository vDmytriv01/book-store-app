package com.vdmytriv.bookstoreapp.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import java.math.BigDecimal;
import java.util.Optional;

public record PartialUpdateBookRequestDto(
        @Schema(description = "Title of the book", example = "Updated Title")
        Optional<String> title,
        @Schema(description = "Author of the book", example = "New Author")
        Optional<String> author,
        @Schema(description = "ISBN number", example = "978-3-16-148410-0")
        Optional<@Pattern(regexp = "^[0-9\\-]*$") String> isbn,
        @Schema(description = "Price of the book", example = "39.99")
        Optional<@Min(0) BigDecimal> price,
        @Schema(description = "Description of the book", example = "Updated description.")
        Optional<String> description,
        @Schema(description = "URL of the book cover image", example = "https://example.com/updated-cover.jpg")
        Optional<String> coverImage
) {
}
