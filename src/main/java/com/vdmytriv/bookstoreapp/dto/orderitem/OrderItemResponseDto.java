package com.vdmytriv.bookstoreapp.dto.orderitem;

public record OrderItemResponseDto(
        Long id,
        Long bookId,
        int quantity
) {
}
