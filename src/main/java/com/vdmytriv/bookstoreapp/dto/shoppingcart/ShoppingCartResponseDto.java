package com.vdmytriv.bookstoreapp.dto.shoppingcart;

import com.vdmytriv.bookstoreapp.dto.cartitem.CartItemResponseDto;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

public record ShoppingCartResponseDto(
        @Schema(description = "ID of the shopping cart", example = "123")
        Long id,

        @Schema(description = "ID of the user who owns the cart", example = "456")
        Long userId,

        @Schema(description = "List of items in the shopping cart")
        List<CartItemResponseDto> cartItems
) {
}
