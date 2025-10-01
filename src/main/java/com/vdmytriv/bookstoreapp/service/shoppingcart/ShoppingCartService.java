package com.vdmytriv.bookstoreapp.service.shoppingcart;

import com.vdmytriv.bookstoreapp.dto.cartitem.CartItemRequestDto;
import com.vdmytriv.bookstoreapp.dto.shoppingcart.ShoppingCartResponseDto;

public interface ShoppingCartService {
    ShoppingCartResponseDto getCart(Long userId);

    ShoppingCartResponseDto addBookToCart(Long userId,
                                          CartItemRequestDto requestDto);

    ShoppingCartResponseDto updateCartItem(Long userId,
                                           Long cartItemId,
                                           int quantity);

    ShoppingCartResponseDto removeCartItem(Long userId, Long cartItemId);
}
