package com.vdmytriv.bookstoreapp.controller;

import com.vdmytriv.bookstoreapp.dto.cartitem.CartItemRequestDto;
import com.vdmytriv.bookstoreapp.dto.cartitem.UpdateCartItemRequestDto;
import com.vdmytriv.bookstoreapp.dto.shoppingcart.ShoppingCartResponseDto;
import com.vdmytriv.bookstoreapp.model.User;
import com.vdmytriv.bookstoreapp.service.shoppingcart.ShoppingCartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/cart")
@Tag(name = "Shopping Cart", description = "Shopping cart management")
@SecurityRequirement(name = "BearerAuth")
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;

    @PreAuthorize("hasRole('USER')")
    @GetMapping
    @Operation(summary = "Retrieve user's shopping cart")
    public ShoppingCartResponseDto getCart(@AuthenticationPrincipal User user) {
        return shoppingCartService.getCart(user.getId());
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping
    @Operation(summary = "Add book to the shopping cart")
    public ShoppingCartResponseDto addBookToCart(
            @AuthenticationPrincipal User user,
            @RequestBody @Valid CartItemRequestDto requestDto) {
        return shoppingCartService.addBookToCart(user.getId(), requestDto);
    }

    @PreAuthorize("hasRole('USER')")
    @PutMapping("/items/{cartItemId}")
    @Operation(summary = "Update quantity of a book in the shopping cart")
    public ShoppingCartResponseDto updateBookQuantity(
            @AuthenticationPrincipal User user,
            @PathVariable Long cartItemId,
            @RequestBody @Valid UpdateCartItemRequestDto updateCartItemRequestDto) {
        return shoppingCartService.updateCartItem(
                user.getId(),
                cartItemId,
                updateCartItemRequestDto.quantity());
    }

    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/items/{cartItemId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Remove a book from the shopping cart")
    public ShoppingCartResponseDto removeCartItem(
            @AuthenticationPrincipal User user,
            @PathVariable Long cartItemId) {
        return shoppingCartService.removeCartItem(user.getId(), cartItemId);
    }

}
