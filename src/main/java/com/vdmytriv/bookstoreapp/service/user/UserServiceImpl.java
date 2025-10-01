package com.vdmytriv.bookstoreapp.service.user;

import com.vdmytriv.bookstoreapp.dto.user.UserRegisterRequestDto;
import com.vdmytriv.bookstoreapp.dto.user.UserResponseDto;
import com.vdmytriv.bookstoreapp.exception.RegistrationException;
import com.vdmytriv.bookstoreapp.mapper.UserMapper;
import com.vdmytriv.bookstoreapp.model.Role;
import com.vdmytriv.bookstoreapp.model.RoleName;
import com.vdmytriv.bookstoreapp.model.ShoppingCart;
import com.vdmytriv.bookstoreapp.model.User;
import com.vdmytriv.bookstoreapp.repository.role.RoleRepository;
import com.vdmytriv.bookstoreapp.repository.shoppingcart.ShoppingCartRepository;
import com.vdmytriv.bookstoreapp.repository.user.UserRepository;
import jakarta.transaction.Transactional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Transactional
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final ShoppingCartRepository shoppingCartRepository;

    @Override
    public UserResponseDto registration(UserRegisterRequestDto requestDto) {
        if (userRepository.existsByEmail(requestDto.email())) {
            throw new RegistrationException("User already exists with email " + requestDto.email());
        }
        Role userRole = roleRepository.findByName(RoleName.USER)
                .orElseThrow(() -> new RuntimeException(
                        "Default role " + RoleName.USER + " not found"));

        User user = userMapper.toModel(requestDto);
        user.setRoles(Set.of(userRole));
        user.setPassword(passwordEncoder.encode(requestDto.password()));
        User savedUser = userRepository.save(user);

        ShoppingCart cart = new ShoppingCart();
        cart.setUser(savedUser);
        shoppingCartRepository.save(cart);
        return userMapper.toDto(savedUser);
    }
}
