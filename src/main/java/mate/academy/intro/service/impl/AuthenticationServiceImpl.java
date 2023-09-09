package mate.academy.intro.service.impl;

import java.util.Set;
import lombok.RequiredArgsConstructor;
import mate.academy.intro.dto.user.UserLoginRequestDto;
import mate.academy.intro.dto.user.UserLoginResponseDto;
import mate.academy.intro.dto.user.UserRegistrationRequestDto;
import mate.academy.intro.dto.user.UserResponseDto;
import mate.academy.intro.exception.RegistrationException;
import mate.academy.intro.mapper.UserMapper;
import mate.academy.intro.model.Role;
import mate.academy.intro.model.ShoppingCart;
import mate.academy.intro.model.User;
import mate.academy.intro.repository.RoleRepository;
import mate.academy.intro.repository.ShoppingCartRepository;
import mate.academy.intro.repository.UserRepository;
import mate.academy.intro.security.JwtUtil;
import mate.academy.intro.service.AuthenticationService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final ShoppingCartRepository shoppingCartRepository;

    @Override
    public UserLoginResponseDto login(UserLoginRequestDto loginRequestDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequestDto.getEmail(),
                        loginRequestDto.getPassword()));
        return new UserLoginResponseDto(jwtUtil
                .generateToken(authentication.getName()));
    }

    @Override
    public UserResponseDto register(UserRegistrationRequestDto request)
            throws RegistrationException {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RegistrationException("Registration error: "
                    + "could not to continue registration");
        }
        User user = userMapper.toModel(request);
        Role userRole = roleRepository.findByName(Role.RoleName.USER).orElseThrow(
                () -> new RegistrationException("Registration error: "
                        + "could not to continue registration"));
        if (SecurityContextHolder.getContext().getAuthentication().getAuthorities()
                .contains(new SimpleGrantedAuthority(Role.RoleName.ADMIN.name()))) {
            Role adminRole = roleRepository.findByName(Role.RoleName.ADMIN).orElseThrow(
                    () -> new RegistrationException("Registration error: "
                            + "could not to continue registration"));
            user.setRoles(Set.of(userRole, adminRole));
        } else {
            user.setRoles(Set.of(userRole));
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user = userRepository.save(user);
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);
        shoppingCartRepository.save(shoppingCart);
        return userMapper.toUserResponseDto(user);
    }
}
