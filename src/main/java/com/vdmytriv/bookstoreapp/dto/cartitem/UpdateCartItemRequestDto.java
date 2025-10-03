package com.vdmytriv.bookstoreapp.dto.cartitem;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;

public record UpdateCartItemRequestDto(
        @Schema(description = "New quantity of the book in the shopping cart", example = "10")
        @Min(1)
        int quantity
) {
}
