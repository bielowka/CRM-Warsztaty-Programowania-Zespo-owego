package com.customer.relationship.management.app.auth;

import com.customer.relationship.management.app.users.UserRole;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthService authService;

    private LoginRequest loginRequest;
    private AuthResponse authResponse;
    private static final String TEST_TOKEN = "test.jwt.token";

    @BeforeEach
    void setUp() {
        loginRequest = new LoginRequest();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("password123");

        authResponse = AuthResponse.builder()
                .token(TEST_TOKEN)
                .userId(1L)
                .email("test@example.com")
                .firstName("Test")
                .lastName("User")
                .role(UserRole.ADMIN)
                .authenticated(true)
                .build();
    }

    @Test
    void login_WithValidCredentials_ShouldReturnAuthResponse() throws Exception {
        // Given
        when(authService.login(any(LoginRequest.class))).thenReturn(authResponse);

        // When & Then
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value(TEST_TOKEN))
                .andExpect(jsonPath("$.authenticated").value(true))
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.role").value(UserRole.ADMIN.name()));
    }

    @Test
    void login_WithInvalidCredentials_ShouldReturnUnauthorized() throws Exception {
        // Given
        when(authService.login(any(LoginRequest.class)))
                .thenThrow(new BadCredentialsException("Invalid credentials"));

        // When & Then
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void validateToken_WithValidToken_ShouldReturnAuthResponse() throws Exception {
        // Given
        when(authService.validateToken(TEST_TOKEN)).thenReturn(authResponse);

        // When & Then
        mockMvc.perform(post("/api/auth/validate")
                .header("Authorization", TEST_TOKEN))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.authenticated").value(true))
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.role").value(UserRole.ADMIN.name()));
    }

    @Test
    void validateToken_WithInvalidToken_ShouldReturnUnauthenticated() throws Exception {
        // Given
        AuthResponse unauthenticatedResponse = AuthResponse.builder()
                .authenticated(false)
                .build();
        when(authService.validateToken(TEST_TOKEN)).thenReturn(unauthenticatedResponse);

        // When & Then
        mockMvc.perform(post("/api/auth/validate")
                .header("Authorization", TEST_TOKEN))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.authenticated").value(false));
    }
} 