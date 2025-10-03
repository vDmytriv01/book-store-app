package com.vdmytriv.bookstoreapp.controller;

import com.vdmytriv.bookstoreapp.dto.order.OrderAddressRequestDto;
import com.vdmytriv.bookstoreapp.dto.order.OrderResponseDto;
import com.vdmytriv.bookstoreapp.dto.order.OrderStatusRequestDto;
import com.vdmytriv.bookstoreapp.dto.orderitem.OrderItemResponseDto;
import com.vdmytriv.bookstoreapp.model.User;
import com.vdmytriv.bookstoreapp.service.order.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping(
        value = "/orders")
@Tag(name = "Orders", description = "Endpoints for managing orders")
@SecurityRequirement(name = "BearerAuth")
public class OrdersController {
    private final OrderService orderService;

    @PreAuthorize("hasRole('USER')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Place an order")
    public OrderResponseDto createOrder(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody OrderAddressRequestDto orderAddressRequestDto) {
        return orderService.createOrder(user, orderAddressRequestDto);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping
    @Operation(summary = "Retrieve user's order history")
    public Page<OrderResponseDto> getOrders(
            @AuthenticationPrincipal User user,
            @ParameterObject Pageable pageable) {
        return orderService.getOrdersHistory(user.getId(), pageable);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}")
    @Operation(summary = "Update order status")
    public OrderResponseDto updateOrderStatus(
            @PathVariable Long id,
            @Valid @RequestBody OrderStatusRequestDto orderStatusRequestDto) {
        return orderService.updateOrderStatus(id, orderStatusRequestDto);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/{id}/items")
    @Operation(summary = "Retrieve all OrderItems for a specific order")
    public Page<OrderItemResponseDto> getAllOrderItems(
            @AuthenticationPrincipal User user,
            @PathVariable Long id,
            @ParameterObject Pageable pageable) {
        return orderService.getAllOrderItems(user.getId(),id, pageable);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/{orderId}/items/{itemId}")
    @Operation(summary = "Retrieve a specific OrderItem within an order")
    public OrderItemResponseDto getOrderItemsById(
            @AuthenticationPrincipal User user,
            @PathVariable Long orderId,
            @PathVariable Long itemId) {
        return orderService.getOrderItemsById(user.getId(),orderId, itemId);

    }
}
