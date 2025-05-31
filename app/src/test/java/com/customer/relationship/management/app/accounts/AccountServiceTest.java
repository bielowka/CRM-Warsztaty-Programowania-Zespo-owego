package com.customer.relationship.management.app.accounts;

import com.customer.relationship.management.app.TestEntitiesUtils;
import com.customer.relationship.management.app.users.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountService accountService;

    private Account testAccount;

    @BeforeEach
    void setUp() {
        User testUser = TestEntitiesUtils.getTestUser("test@example.com");
        testAccount = TestEntitiesUtils.getTestAccount(testUser, "account@example.com");
        testAccount.setId(1L);
    }

    @Test
    void findAll_ShouldReturnAllAccounts() {
        // Given
        List<Account> expectedAccounts = Collections.singletonList(testAccount);
        when(accountRepository.findAll()).thenReturn(expectedAccounts);

        // When
        List<Account> actualAccounts = accountService.findAll();

        // Then
        assertEquals(expectedAccounts, actualAccounts);
        verify(accountRepository).findAll();
    }

    @Test
    void findById_WithExistingId_ShouldReturnAccount() {
        // Given
        when(accountRepository.findById(1L)).thenReturn(Optional.of(testAccount));

        // When
        Optional<Account> result = accountService.findById(1L);

        // Then
        assertTrue(result.isPresent());
        assertEquals(testAccount, result.get());
        verify(accountRepository).findById(1L);
    }

    @Test
    void findById_WithNonExistingId_ShouldReturnEmpty() {
        // Given
        when(accountRepository.findById(999L)).thenReturn(Optional.empty());

        // When
        Optional<Account> result = accountService.findById(999L);

        // Then
        assertTrue(result.isEmpty());
        verify(accountRepository).findById(999L);
    }

    @Test
    void save_WithValidAccount_ShouldSaveAndReturnAccount() {
        // Given
        when(accountRepository.save(testAccount)).thenReturn(testAccount);

        // When
        Account savedAccount = accountService.save(testAccount);

        // Then
        assertEquals(testAccount, savedAccount);
        verify(accountRepository).save(testAccount);
    }

    @Test
    void save_WithNullUser_ShouldThrowException() {
        // Given
        Account invalidAccount = new Account();
        invalidAccount.setEmail("test@example.com");
        invalidAccount.setFirstName("Test");
        invalidAccount.setLastName("Account");
        invalidAccount.setAccountStatus(AccountStatus.ACTIVE);
        invalidAccount.setPhoneNumber("+1234567890");
        invalidAccount.setUser(null);

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> accountService.save(invalidAccount));
        verify(accountRepository, never()).save(any());
    }

    @Test
    void deleteById_ShouldCallRepositoryDelete() {
        // When
        accountService.deleteById(1L);

        // Then
        verify(accountRepository).deleteById(1L);
    }
} 