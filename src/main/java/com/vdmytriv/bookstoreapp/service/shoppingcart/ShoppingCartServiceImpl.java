package com.vdmytriv.bookstoreapp.service.shoppingcart;

import com.vdmytriv.bookstoreapp.dto.cartitem.CartItemRequestDto;
import com.vdmytriv.bookstoreapp.dto.shoppingcart.ShoppingCartResponseDto;
import com.vdmytriv.bookstoreapp.exception.EntityNotFoundException;
import com.vdmytriv.bookstoreapp.mapper.CartItemMapper;
import com.vdmytriv.bookstoreapp.mapper.ShoppingCartMapper;
import com.vdmytriv.bookstoreapp.model.Book;
import com.vdmytriv.bookstoreapp.model.CartItem;
import com.vdmytriv.bookstoreapp.model.ShoppingCart;
import com.vdmytriv.bookstoreapp.repository.book.BookRepository;
import com.vdmytriv.bookstoreapp.repository.shoppingcart.ShoppingCartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final BookRepository bookRepository;
    private final ShoppingCartMapper shoppingCartMapper;
    private final CartItemMapper cartItemMapper;

    @Transactional(readOnly = true)
    @Override
    public ShoppingCartResponseDto getCart(Long userId) {
        return shoppingCartRepository.findByUserId(userId)
                .map(shoppingCartMapper::toDto)
                .orElseThrow(() ->
                        new EntityNotFoundException("Shopping cart not found for user " + userId));
    }

    @Transactional
    @Override
    public ShoppingCartResponseDto addBookToCart(Long userId,
                                                 CartItemRequestDto cartItemRequestDto) {
        ShoppingCart shoppingCart = findShoppingCartByUserId(userId);
        Book book = bookRepository.findById(cartItemRequestDto.bookId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Book not found with id: " + cartItemRequestDto.bookId()));

        CartItem existingCartItem = shoppingCart.getCartItems().stream()
                .filter(cartItem -> cartItem.getBook().getId().equals(book.getId()))
                .findFirst()
                .orElse(null);
        if (existingCartItem != null) {
            existingCartItem.setQuantity(existingCartItem.getQuantity()
                    + cartItemRequestDto.quantity());
        } else {
            CartItem newItem = cartItemMapper.toModel(cartItemRequestDto);
            newItem.setBook(book);
            newItem.setShoppingCart(shoppingCart);
            shoppingCart.getCartItems().add(newItem);
        }
        shoppingCartRepository.save(shoppingCart);
        return shoppingCartMapper.toDto(shoppingCart);
    }

    @Transactional
    @Override
    public ShoppingCartResponseDto updateCartItem(Long userId, Long cartItemId, int quantity) {
        ShoppingCart shoppingCart = findShoppingCartByUserId(userId);
        CartItem cartItem = findCartItemById(shoppingCart, cartItemId);
        cartItem.setQuantity(quantity);
        return shoppingCartMapper.toDto(shoppingCart);
    }

    @Transactional
    @Override
    public ShoppingCartResponseDto removeCartItem(Long userId, Long cartItemId) {
        ShoppingCart shoppingCart = findShoppingCartByUserId(userId);
        CartItem cartItem = findCartItemById(shoppingCart, cartItemId);
        shoppingCart.getCartItems().remove(cartItem);
        return shoppingCartMapper.toDto(shoppingCart);
    }

    private ShoppingCart findShoppingCartByUserId(Long userId) {
        return shoppingCartRepository.findByUserId(userId)
                .orElseThrow(() ->
                        new EntityNotFoundException("Shopping cart not found for user " + userId));
    }

    private CartItem findCartItemById(ShoppingCart shoppingCart, Long cartItemId) {
        return shoppingCart.getCartItems().stream()
                .filter(item -> item.getId().equals(cartItemId))
                .findFirst()
                .orElseThrow(() ->
                        new EntityNotFoundException("Cart item not found: " + cartItemId));
    }
}
