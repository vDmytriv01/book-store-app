package com.vdmytriv.bookstoreapp.repository.role;

import com.vdmytriv.bookstoreapp.model.Role;
import com.vdmytriv.bookstoreapp.model.RoleName;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleName name);
}
