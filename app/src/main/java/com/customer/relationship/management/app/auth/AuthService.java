package com.customer.relationship.management.app.auth;

import com.customer.relationship.management.app.users.User;
import com.customer.relationship.management.app.users.UserRepository;
import io.jsonwebtoken.Claims;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public AuthResponse login(LoginRequest loginRequest) {
        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new BadCredentialsException("Invalid credentials"));

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Invalid credentials");
        }

        String token = jwtUtil.generateToken(user);
        return buildAuthResponse(user, token);
    }

    public AuthResponse validateToken(String authHeader) {
        try {
            Claims claims = jwtUtil.validateToken(authHeader);
            User user = userRepository.findByEmail(claims.getSubject())
                    .orElseThrow(() -> new BadCredentialsException("User not found"));
            
            return buildAuthResponse(user, authHeader);
        } catch (Exception e) {
            return AuthResponse.builder()
                    .authenticated(false)
                    .build();
        }
    }

    private AuthResponse buildAuthResponse(User user, String token) {
        return AuthResponse.builder()
                .token(token)
                .userId(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .role(user.getRole())
                .authenticated(true)
                .build();
    }
} 