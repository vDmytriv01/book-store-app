package com.vdmytriv.bookstoreapp.service.order;

import static com.vdmytriv.bookstoreapp.model.Status.PENDING;

import com.vdmytriv.bookstoreapp.dto.order.OrderAddressRequestDto;
import com.vdmytriv.bookstoreapp.dto.order.OrderResponseDto;
import com.vdmytriv.bookstoreapp.dto.order.OrderStatusRequestDto;
import com.vdmytriv.bookstoreapp.dto.orderitem.OrderItemResponseDto;
import com.vdmytriv.bookstoreapp.exception.BadRequestException;
import com.vdmytriv.bookstoreapp.exception.EntityNotFoundException;
import com.vdmytriv.bookstoreapp.mapper.OrderItemMapper;
import com.vdmytriv.bookstoreapp.mapper.OrderMapper;
import com.vdmytriv.bookstoreapp.model.CartItem;
import com.vdmytriv.bookstoreapp.model.Order;
import com.vdmytriv.bookstoreapp.model.OrderItem;
import com.vdmytriv.bookstoreapp.model.ShoppingCart;
import com.vdmytriv.bookstoreapp.model.User;
import com.vdmytriv.bookstoreapp.repository.order.OrderRepository;
import com.vdmytriv.bookstoreapp.repository.orderitem.OrderItemRepository;
import com.vdmytriv.bookstoreapp.repository.shoppingcart.ShoppingCartRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final ShoppingCartRepository shoppingCartRepository;
    private final OrderItemRepository orderItemRepository;
    private final OrderItemMapper orderItemMapper;

    @Override
    @Transactional
    public OrderResponseDto createOrder(User user, OrderAddressRequestDto requestDto) {
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(user.getId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Shopping cart not found for user " + user.getId()));
        if (shoppingCart.getCartItems().isEmpty()) {
            throw new BadRequestException("Cannot create order from empty shopping cart");
        }
        Order order = new Order();
        order.setUser(user);
        order.setStatus(PENDING);
        order.setOrderDate(LocalDateTime.now());
        order.setShippingAddress(requestDto.shippingAddress());

        Set<OrderItem> orderItems = shoppingCart.getCartItems().stream()
                .map(cartItem -> toOrderItem(cartItem, order))
                .collect(Collectors.toSet());
        order.setOrderItems(orderItems);
        order.setTotal(orderItems.stream()
                .map(orderItem -> orderItem.getPrice()
                        .multiply(BigDecimal.valueOf(orderItem.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        shoppingCart.getCartItems().clear();
        shoppingCartRepository.save(shoppingCart);

        return orderMapper.toDto(orderRepository.save(order));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OrderResponseDto> getOrdersHistory(Long userId, Pageable pageable) {
        return orderRepository.findByUserId(userId, pageable).map(orderMapper::toDto);
    }

    @Override
    @Transactional
    public OrderResponseDto updateOrderStatus(Long orderId,
                                              OrderStatusRequestDto orderStatusRequestDto) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Order not found with id  " + orderId));
        order.setStatus(orderStatusRequestDto.status());
        return orderMapper.toDto(orderRepository.save(order));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OrderItemResponseDto> getAllOrderItems(Long userId,
                                                       Long orderId,
                                                       Pageable pageable) {
        findOrderByIdAndUserId(orderId, userId);

        return orderItemRepository.findByOrderId(orderId, pageable)
                .map(orderItemMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderItemResponseDto getOrderItemsById(Long userId, Long orderId, Long itemId) {
        findOrderByIdAndUserId(orderId, userId);

        return orderItemRepository.findByIdAndOrderId(itemId, orderId)
                .map(orderItemMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Order item not found with id=" + itemId + " for order id=" + orderId));
    }

    private OrderItem toOrderItem(CartItem cartItem, Order order) {
        OrderItem orderItem = new OrderItem();
        orderItem.setOrder(order);
        orderItem.setBook(cartItem.getBook());
        orderItem.setQuantity(cartItem.getQuantity());
        orderItem.setPrice(cartItem.getBook().getPrice());
        return orderItem;
    }

    private Order findOrderByIdAndUserId(Long orderId, Long userId) {
        return orderRepository.findByIdAndUserId(orderId, userId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Order not found with id=" + orderId + " for user id=" + userId));
    }
}
