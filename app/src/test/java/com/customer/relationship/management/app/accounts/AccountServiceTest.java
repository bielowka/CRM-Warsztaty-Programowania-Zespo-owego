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

    private AccountInfo testAccountInfo;

    @BeforeEach
    void setUp() {
        User testUser = TestEntitiesUtils.getTestUser("test@example.com");
        testAccount = AccountsFixture.getTestAccount(testUser, "account@example.com");
        testAccount.setId(1L);
        testAccountInfo = new AccountInfo() {

            @Override
            public Long getId() {
                return testAccount.getId();
            }

            @Override
            public String getFirstName() {
                return testAccount.getFirstName();
            }

            @Override
            public String getLastName() {
                return testAccount.getLastName();
            }

            @Override
            public String getEmail() {
                return testAccount.getEmail();
            }

            @Override
            public AccountStatus getAccountStatus() {
                return testAccount.getAccountStatus();
            }

            @Override
            public String getPhoneNumber() {
                return testAccount.getPhoneNumber();
            }

            @Override
            public String getCompanyName() {
                return "ABC Corp";
            }
        };
    }

    @Test
    void findAll_ShouldReturnAllAccounts() {
        // Given
        List<AccountInfo> expectedAccounts = Collections.singletonList(testAccountInfo);
        when(accountRepository.findAllBy()).thenReturn(expectedAccounts);

        // When
        List<AccountInfo> actualAccounts = accountService.findAll();

        // Then
        assertEquals(expectedAccounts, actualAccounts);
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