package com.vdmytriv.bookstoreapp.dto;

import com.vdmytriv.bookstoreapp.validation.PriceRangeValidation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import java.math.BigDecimal;

@PriceRangeValidation
public record BookSearchParametersDto(
        @ArraySchema(
                schema = @Schema(description = "Titles to search for", example = "Effective Java")
        )
        String[] title,
        @ArraySchema(
                schema = @Schema(description = "Authors to search for", example = "Joshua Bloch")
        )
        String[] author,
        @ArraySchema(
                schema = @Schema(description = "ISBN numbers to search for",
                        example = "978-3-16-148410-0")
        )
        String[] isbn,
        @Schema(description = "Minimum price", example = "10.00")
        @Min(0)
        BigDecimal minPrice,
        @Schema(description = "Maximum price", example = "100.00")
        @Min(0)
        BigDecimal maxPrice
) {
}
