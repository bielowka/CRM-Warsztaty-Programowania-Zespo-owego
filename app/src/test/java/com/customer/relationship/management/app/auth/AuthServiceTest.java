package com.customer.relationship.management.app.auth;

import com.customer.relationship.management.app.users.User;
import com.customer.relationship.management.app.users.UserRepository;
import com.customer.relationship.management.app.users.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Claims;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {


    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthService authService;

    private User testUser;
    private LoginRequest loginRequest;
    private static final String TEST_TOKEN = "test.jwt.token";
    public static final String TEST_EMAIL = "test@example.com";
    public static final String TEST_PASSWORD = "password123";

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setEmail(TEST_EMAIL);
        testUser.setPassword("encodedPassword");
        testUser.setFirstName("Test");
        testUser.setLastName("User");
        testUser.setRole(UserRole.ADMIN);

        loginRequest = new LoginRequest();
        loginRequest.setEmail(TEST_EMAIL);
        loginRequest.setPassword(TEST_PASSWORD);
    }

    @Test
    void login_WithValidCredentials_ShouldReturnAuthResponse() {
        // Given
        when(userRepository.findByEmail(TEST_EMAIL)).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches(TEST_PASSWORD, "encodedPassword")).thenReturn(true);
        when(jwtUtil.generateToken(any(User.class))).thenReturn(TEST_TOKEN);

        // When
        AuthResponse response = authService.login(loginRequest);

        // Then
        assertNotNull(response);
        assertTrue(response.isAuthenticated());
        assertEquals(TEST_TOKEN, response.getToken());
        assertEquals(testUser.getEmail(), response.getEmail());
        assertEquals(testUser.getRole(), response.getRole());
    }

    @Test
    void login_WithInvalidEmail_ShouldThrowBadCredentialsException() {
        // Given
        when(userRepository.findByEmail(TEST_EMAIL)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(BadCredentialsException.class, () -> authService.login(loginRequest));
    }

    @Test
    void login_WithInvalidPassword_ShouldThrowBadCredentialsException() {
        // Given
        when(userRepository.findByEmail(TEST_EMAIL)).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches(TEST_PASSWORD, "encodedPassword")).thenReturn(false);

        // When & Then
        assertThrows(BadCredentialsException.class, () -> authService.login(loginRequest));
    }

    @Test
    void validateToken_WithValidToken_ShouldReturnAuthResponse() {
        // Given
        when(jwtUtil.validateToken(TEST_TOKEN)).thenReturn(createTestClaims());
        when(userRepository.findByEmail(TEST_EMAIL)).thenReturn(Optional.of(testUser));

        // When
        AuthResponse response = authService.validateToken(TEST_TOKEN);

        // Then
        assertNotNull(response);
        assertTrue(response.isAuthenticated());
        assertEquals(testUser.getEmail(), response.getEmail());
        assertEquals(testUser.getRole(), response.getRole());
    }

    private Claims createTestClaims() {
        return Jwts.claims().setSubject(AuthServiceTest.TEST_EMAIL);
    }

    @Test
    void validateToken_WithInvalidToken_ShouldReturnUnauthenticatedResponse() {
        // Given
        when(jwtUtil.validateToken(TEST_TOKEN)).thenThrow(new RuntimeException("Invalid token"));

        // When
        AuthResponse response = authService.validateToken(TEST_TOKEN);

        // Then
        assertNotNull(response);
        assertFalse(response.isAuthenticated());
    }

    @Test
    void validateToken_WithValidTokenButUserNotFound_ShouldReturnUnauthenticatedResponse() {
        // Given
        when(jwtUtil.validateToken(TEST_TOKEN)).thenReturn(createTestClaims());
        when(userRepository.findByEmail(TEST_EMAIL)).thenReturn(Optional.empty());

        // When
        AuthResponse response = authService.validateToken(TEST_TOKEN);

        // Then
        assertNotNull(response);
        assertFalse(response.isAuthenticated());
    }
} 