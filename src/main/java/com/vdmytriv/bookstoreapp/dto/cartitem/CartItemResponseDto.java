package com.vdmytriv.bookstoreapp.dto.cartitem;

import io.swagger.v3.oas.annotations.media.Schema;

public record CartItemResponseDto(
        @Schema(description = "ID of the cart item", example = "1")
        Long id,

        @Schema(description = "ID of the book in the cart", example = "2")
        Long bookId,

        @Schema(description = "Title of the book", example = "Effective Java")
        String bookTitle,

        @Schema(description = "Quantity of the selected book", example = "5")
        int quantity
) {
}
