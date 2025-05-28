package com.customer.relationship.management.app.auth;

import com.customer.relationship.management.app.users.User;
import com.customer.relationship.management.app.users.UserRole;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {

    private JwtUtil jwtUtil;
    private User testUser;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
        
        testUser = new User();
        testUser.setId(1L);
        testUser.setEmail("test@example.com");
        testUser.setFirstName("Test");
        testUser.setLastName("User");
        testUser.setRole(UserRole.ADMIN);
    }

    @Test
    void generateToken_ShouldCreateValidToken() {
        // When
        String token = jwtUtil.generateToken(testUser);

        // Then
        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    void validateToken_WithValidToken_ShouldReturnClaims() {
        // Given
        String token = jwtUtil.generateToken(testUser);

        // When
        Claims claims = jwtUtil.validateToken("Bearer " + token);

        // Then
        assertNotNull(claims);
        assertEquals(testUser.getEmail(), claims.getSubject());
        assertEquals(UserRole.ADMIN.name(), claims.get("role", String.class));
        assertEquals(1L, claims.get("userId", Long.class));
        assertEquals(testUser.getEmail(), claims.get("email", String.class));
    }

    @Test
    void validateToken_WithInvalidToken_ShouldThrowException() {
        // Given
        String invalidToken = "invalid.token.example";

        // When & Then
        assertThrows(RuntimeException.class, () -> jwtUtil.validateToken("Bearer " + invalidToken));
    }

    @Test
    void validateToken_WithoutBearerPrefix_ShouldThrowException() {
        // Given
        String token = jwtUtil.generateToken(testUser);

        // When & Then
        assertThrows(RuntimeException.class, () -> jwtUtil.validateToken(token));
    }

    @Test
    void validateToken_WithEmptyToken_ShouldThrowException() {
        // When & Then
        assertThrows(RuntimeException.class, () -> jwtUtil.validateToken(""));
    }

    @Test
    void validateToken_WithNullToken_ShouldThrowException() {
        // When & Then
        assertThrows(RuntimeException.class, () -> jwtUtil.validateToken(null));
    }
} 