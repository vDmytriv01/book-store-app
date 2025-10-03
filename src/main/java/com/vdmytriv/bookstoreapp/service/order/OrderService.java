package com.vdmytriv.bookstoreapp.service.order;

import com.vdmytriv.bookstoreapp.dto.order.OrderAddressRequestDto;
import com.vdmytriv.bookstoreapp.dto.order.OrderResponseDto;
import com.vdmytriv.bookstoreapp.dto.order.OrderStatusRequestDto;
import com.vdmytriv.bookstoreapp.dto.orderitem.OrderItemResponseDto;
import com.vdmytriv.bookstoreapp.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderService {
    OrderResponseDto createOrder(User user,
                                 OrderAddressRequestDto requestDto);

    Page<OrderResponseDto> getOrdersHistory(Long useId,
                                            Pageable pageable);

    OrderResponseDto updateOrderStatus(Long orderId,
                                       OrderStatusRequestDto orderStatusRequestDto);

    Page<OrderItemResponseDto> getAllOrderItems(Long userId,
                                                Long orderId,
                                                Pageable pageable);

    OrderItemResponseDto getOrderItemsById(Long userId,
                                           Long orderId,
                                           Long itemId);
}
