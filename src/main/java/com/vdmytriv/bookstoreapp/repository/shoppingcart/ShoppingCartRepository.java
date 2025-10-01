package com.vdmytriv.bookstoreapp.repository.shoppingcart;

import com.vdmytriv.bookstoreapp.model.ShoppingCart;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShoppingCartRepository extends
        JpaRepository<ShoppingCart, Long> {

    @EntityGraph(attributePaths = {"cartItems", "cartItems.book","user"})
    Optional<ShoppingCart> findByUserId(Long userId);
}
