package com.vdmytriv.bookstoreapp.dto.order;

import com.vdmytriv.bookstoreapp.model.Status;
import jakarta.validation.constraints.NotNull;

public record OrderStatusRequestDto(
        @NotNull
        Status status
) {
}
