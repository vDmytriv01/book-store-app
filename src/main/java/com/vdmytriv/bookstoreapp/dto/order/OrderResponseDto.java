package com.vdmytriv.bookstoreapp.dto.order;

import com.vdmytriv.bookstoreapp.dto.orderitem.OrderItemResponseDto;
import com.vdmytriv.bookstoreapp.model.Status;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OrderResponseDto(
        Long id,
        Long userId,
        List<OrderItemResponseDto> orderItems,
        LocalDateTime orderDate,
        BigDecimal total,
        Status status
) {
}
