package com.vdmytriv.bookstoreapp.repository.user;

import com.vdmytriv.bookstoreapp.model.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);
}
