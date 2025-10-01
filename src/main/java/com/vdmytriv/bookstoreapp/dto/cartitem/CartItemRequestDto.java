package com.vdmytriv.bookstoreapp.dto.cartitem;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record CartItemRequestDto(
        @Schema(description = "ID of the book to add to cart", example = "2")
        @NotNull
        Long bookId,

        @Schema(description = "Quantity of the selected book", example = "5")
        @Min(1)
        int quantity
) {
}
