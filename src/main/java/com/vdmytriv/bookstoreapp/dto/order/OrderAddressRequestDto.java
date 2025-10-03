package com.vdmytriv.bookstoreapp.dto.order;

import jakarta.validation.constraints.NotBlank;

public record OrderAddressRequestDto(
        @NotBlank
        String shippingAddress
) {
}
