package com.vdmytriv.bookstoreapp.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import java.math.BigDecimal;
import java.util.Optional;

public record PartialUpdateBookRequestDto(
        Optional<String> title,
        Optional<String> author,
        Optional<@Pattern(regexp = "^[0-9\\-]*$")String> isbn,
        Optional<@Min(0) BigDecimal> price,
        Optional<String> description,
        Optional<String> coverImage
) {
}
