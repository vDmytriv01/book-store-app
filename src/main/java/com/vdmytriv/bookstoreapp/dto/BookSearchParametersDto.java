package com.vdmytriv.bookstoreapp.dto;

import com.vdmytriv.bookstoreapp.validation.PriceRangeValidation;
import jakarta.validation.constraints.Min;
import java.math.BigDecimal;

@PriceRangeValidation
public record BookSearchParametersDto(
        String[] title,
        String[] author,
        String[] isbn,
        @Min(0)
        BigDecimal minPrice,
        @Min(0)
        BigDecimal maxPrice
) {
}
