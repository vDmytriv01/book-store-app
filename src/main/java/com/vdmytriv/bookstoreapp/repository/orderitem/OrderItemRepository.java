package com.vdmytriv.bookstoreapp.repository.orderitem;

import com.vdmytriv.bookstoreapp.model.OrderItem;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    Page<OrderItem> findByOrderId(Long orderId,
                                  Pageable pageable);

    Optional<OrderItem> findByIdAndOrderId(Long itemId, Long orderId);
}
