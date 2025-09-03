package com.debroop.FixMate.auth;

import java.util.List;
import java.util.Map;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.debroop.FixMate.auth.dto.LoginRequest;
import com.debroop.FixMate.auth.dto.RegisterRequest;
import com.debroop.FixMate.domain.User;
import com.debroop.FixMate.repo.UserRepo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService implements UserDetailsService {

    private final UserRepo users;
    private final PasswordEncoder encoder;
    private final JWTService jwt;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User u = users.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException(email));
        return new org.springframework.security.core.userdetails.User(
                u.getEmail(), u.getPassword(),
                List.of(new SimpleGrantedAuthority("ROLE_" + u.getRole())));
    }

    // ⬇️ change: return token after saving the user
    public String register(RegisterRequest req) {
        users.findByEmail(req.email()).ifPresent(u -> {
            throw new IllegalStateException("Email already in use");
        });

        String role = switch (req.role()) {
            case "CUSTOMER", "TECHNICIAN", "ADMIN" -> req.role();
            default -> throw new IllegalArgumentException("Invalid role");
        };

        User user = User.builder()
                .email(req.email().trim().toLowerCase())
                .password(encoder.encode(req.password()))
                .fullName(req.fullName())
                .role(role)
                .build();

        user = users.save(user);

        // issue JWT immediately
        return jwt.generate(
                user.getEmail(),
                Map.of("uid", user.getId(), "role", user.getRole()));
    }

    public String login(LoginRequest req) {
        User user = users.findByEmail(req.email()).orElseThrow(
                () -> new UsernameNotFoundException("User not found"));
        if (!encoder.matches(req.password(), user.getPassword())) {
            throw new BadCredentialsException("Invalid credentials");
        }
        return jwt.generate(
                user.getEmail(),
                Map.of("uid", user.getId(), "role", user.getRole()));
    }
}