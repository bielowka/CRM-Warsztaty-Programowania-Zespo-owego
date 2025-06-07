package com.customer.relationship.management.app.users;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private User testUser;
    private static final String TEST_EMAIL = "test@example.com";
    private static final String TEST_PASSWORD = "password123";
    private static final String ENCODED_PASSWORD = "encodedPassword123";

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setEmail(TEST_EMAIL);
        testUser.setPassword(TEST_PASSWORD);
        testUser.setFirstName("Test");
        testUser.setLastName("User");
        testUser.setRole(UserRole.ADMIN);
        testUser.setPosition("Manager");
        testUser.setActive(true);
    }

    @Test
    void createUser_ShouldEncodePasswordAndSaveUser() {
        // Given
        when(passwordEncoder.encode(TEST_PASSWORD)).thenReturn(ENCODED_PASSWORD);
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // When
        User createdUser = userService.createUser(testUser);

        // Then
        verify(passwordEncoder).encode(TEST_PASSWORD);
        verify(userRepository).save(testUser);
        assertEquals(ENCODED_PASSWORD, testUser.getPassword());
        assertNotNull(createdUser);
    }

    @Test
    void getAllUsers_ShouldReturnListOfUsers() {
        // Given
        List<User> expectedUsers = Collections.singletonList(testUser);
        when(userRepository.findAll()).thenReturn(expectedUsers);

        // When
        List<User> actualUsers = userService.getAllUsers();

        // Then
        verify(userRepository).findAll();
        assertEquals(expectedUsers, actualUsers);
    }

    @Test
    void getUserById_WithValidId_ShouldReturnUser() {
        // Given
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        // When
        User foundUser = userService.getUserById(1L);

        // Then
        verify(userRepository).findById(1L);
        assertEquals(testUser, foundUser);
    }

    @Test
    void getUserById_WithInvalidId_ShouldThrowException() {
        // Given
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(EntityNotFoundException.class, () -> userService.getUserById(999L));
        verify(userRepository).findById(999L);
    }

    @Test
    void updateUser_WithValidId_ShouldUpdateAndReturnUser() {
        // Given
        User updatedDetails = new User();
        updatedDetails.setFirstName("Updated");
        updatedDetails.setLastName("Name");
        updatedDetails.setEmail("updated@example.com");
        updatedDetails.setRole(UserRole.SALESPERSON);
        updatedDetails.setPosition("Developer");
        updatedDetails.setActive(true);
        updatedDetails.setPassword("newPassword");

        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(passwordEncoder.encode("newPassword")).thenReturn("encodedNewPassword");
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // When
        User updatedUser = userService.updateUser(1L, updatedDetails);

        // Then
        verify(userRepository).findById(1L);
        verify(passwordEncoder).encode("newPassword");
        verify(userRepository).save(testUser);
        
        assertEquals(updatedDetails.getFirstName(), updatedUser.getFirstName());
        assertEquals(updatedDetails.getLastName(), updatedUser.getLastName());
        assertEquals(updatedDetails.getEmail(), updatedUser.getEmail());
        assertEquals(updatedDetails.getRole(), updatedUser.getRole());
        assertEquals(updatedDetails.getPosition(), updatedUser.getPosition());
        assertEquals(updatedDetails.isActive(), updatedUser.isActive());
    }

    @Test
    void updateUser_WithInvalidId_ShouldThrowException() {
        // Given
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(EntityNotFoundException.class, () -> userService.updateUser(999L, new User()));
        verify(userRepository).findById(999L);
        verify(userRepository, never()).save(any());
    }

    @Test
    void deleteUser_WithValidId_ShouldDeleteUser() {
        // Given
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        // When
        userService.deleteUser(1L);

        // Then
        verify(userRepository).findById(1L);
        verify(userRepository).delete(testUser);
    }

    @Test
    void deleteUser_WithInvalidId_ShouldThrowException() {
        // Given
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(EntityNotFoundException.class, () -> userService.deleteUser(999L));
        verify(userRepository).findById(999L);
        verify(userRepository, never()).delete(any());
    }

    @Test
    void changeUserPassword_WithValidCredentials_ShouldUpdatePassword() {
        // Given
        testUser.setPassword("currentEncodedPassword");
        when(userRepository.findByEmail(TEST_EMAIL)).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches(TEST_PASSWORD, "currentEncodedPassword")).thenReturn(true);
        String encodedNewPassword = "encodedNewPassword";
        when(passwordEncoder.encode("newPassword")).thenReturn(encodedNewPassword);

        // When
        userService.changeUserPassword(TEST_EMAIL, TEST_PASSWORD, "newPassword");

        // Then
        verify(userRepository).findByEmail(TEST_EMAIL);
        verify(passwordEncoder).matches(TEST_PASSWORD, "currentEncodedPassword");
        verify(passwordEncoder).encode("newPassword");
        verify(userRepository).save(argThat(user -> 
            user.getPassword().equals(encodedNewPassword)
        ));
    }

    @Test
    void changeUserPassword_WithInvalidEmail_ShouldThrowException() {
        // Given
        when(userRepository.findByEmail("invalid@example.com")).thenReturn(Optional.empty());

        // When & Then
        assertThrows(EntityNotFoundException.class, 
            () -> userService.changeUserPassword("invalid@example.com", TEST_PASSWORD, "newPassword"));
        verify(userRepository).findByEmail("invalid@example.com");
        verify(passwordEncoder, never()).matches(any(), any());
        verify(passwordEncoder, never()).encode(any());
        verify(userRepository, never()).save(any());
    }

    @Test
    void changeUserPassword_WithIncorrectCurrentPassword_ShouldThrowException() {
        // Given
        when(userRepository.findByEmail(TEST_EMAIL)).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches(TEST_PASSWORD, testUser.getPassword())).thenReturn(false);

        // When & Then
        assertThrows(BadCredentialsException.class,
            () -> userService.changeUserPassword(TEST_EMAIL, TEST_PASSWORD, "newPassword"));
        verify(userRepository).findByEmail(TEST_EMAIL);
        verify(passwordEncoder).matches(TEST_PASSWORD, testUser.getPassword());
        verify(passwordEncoder, never()).encode(any());
        verify(userRepository, never()).save(any());
    }
} 