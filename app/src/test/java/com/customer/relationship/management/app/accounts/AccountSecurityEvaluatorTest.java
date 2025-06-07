package com.customer.relationship.management.app.accounts;

import com.customer.relationship.management.app.users.User;
import com.customer.relationship.management.app.users.UserRole;
import com.customer.relationship.management.app.users.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountSecurityEvaluatorTest {

    @Mock
    private AccountService accountService;

    @Mock
    private UserService userService;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private AccountSecurityEvaluator securityEvaluator;

    private static final String SALESPERSON_EMAIL = "sales@example.com";
    private static final Long ACCOUNT_ID = 1L;
    private static final Long USER_ID = 1L;

    @BeforeEach
    void setUp() {
        when(authentication.isAuthenticated()).thenReturn(true);
    }

    @Test
    void canAccessAccount_WithAdmin_ShouldReturnTrue() {
        // Given
        mockUserRole(UserRole.ADMIN);

        // When
        boolean result = securityEvaluator.canAccessAccount(authentication, ACCOUNT_ID);

        // Then
        assertTrue(result);
        verify(accountService, never()).findById(any());
    }

    @Test
    void canAccessAccount_WithManager_ShouldReturnTrue() {
        // Given
        mockUserRole(UserRole.MANAGER);

        // When
        boolean result = securityEvaluator.canAccessAccount(authentication, ACCOUNT_ID);

        // Then
        assertTrue(result);
        verify(accountService, never()).findById(any());
    }

    @Test
    void canAccessAccount_WithSalespersonOwningAccount_ShouldReturnTrue() {
        // Given
        mockUserRole(UserRole.SALESPERSON);
        when(authentication.getName()).thenReturn(SALESPERSON_EMAIL);

        Account account = new Account();
        User user = new User();
        user.setEmail(SALESPERSON_EMAIL);
        account.setUser(user);

        when(accountService.findById(ACCOUNT_ID)).thenReturn(Optional.of(account));

        // When
        boolean result = securityEvaluator.canAccessAccount(authentication, ACCOUNT_ID);

        // Then
        assertTrue(result);
        verify(accountService).findById(ACCOUNT_ID);
    }

    @Test
    void canAccessAccount_WithSalespersonNotOwningAccount_ShouldReturnFalse() {
        // Given
        mockUserRole(UserRole.SALESPERSON);
        when(authentication.getName()).thenReturn(SALESPERSON_EMAIL);

        Account account = new Account();
        User user = new User();
        user.setEmail("other@example.com");
        account.setUser(user);

        when(accountService.findById(ACCOUNT_ID)).thenReturn(Optional.of(account));

        // When
        boolean result = securityEvaluator.canAccessAccount(authentication, ACCOUNT_ID);

        // Then
        assertFalse(result);
        verify(accountService).findById(ACCOUNT_ID);
    }

    @Test
    void canAccessUserAccounts_WithAdmin_ShouldReturnTrue() {
        // Given
        mockUserRole(UserRole.ADMIN);

        // When
        boolean result = securityEvaluator.canAccessUserAccounts(authentication, USER_ID);

        // Then
        assertTrue(result);
        verify(accountService, never()).findAllByUserId(any());
    }

    @Test
    void canAccessUserAccounts_WithManager_ShouldReturnTrue() {
        // Given
        mockUserRole(UserRole.MANAGER);

        // When
        boolean result = securityEvaluator.canAccessUserAccounts(authentication, USER_ID);

        // Then
        assertTrue(result);
        verify(accountService, never()).findAllByUserId(any());
    }

    @Test
    void canAccessUserAccounts_WithSalespersonForOwnUserId_ShouldReturnTrue() {
        // Given
        mockUserRole(UserRole.SALESPERSON);
        when(authentication.getName()).thenReturn(SALESPERSON_EMAIL);

        User user = new User();
        user.setEmail(SALESPERSON_EMAIL);

        when(userService.getUserById(USER_ID)).thenReturn(user);

        // When
        boolean result = securityEvaluator.canAccessUserAccounts(authentication, USER_ID);

        // Then
        assertTrue(result);
    }

    @Test
    void canAccessUserAccounts_WithSalespersonForOtherUserId_ShouldReturnFalse() {
        // Given
        mockUserRole(UserRole.SALESPERSON);
        when(authentication.getName()).thenReturn(SALESPERSON_EMAIL);

        User user = new User();
        user.setEmail("other@example.com");

        when(userService.getUserById(USER_ID)).thenReturn(user);

        // When
        boolean result = securityEvaluator.canAccessUserAccounts(authentication, USER_ID);

        // Then
        assertFalse(result);
    }

    @Test
    void canCreateAccount_WithAdmin_ShouldReturnTrue() {
        // Given
        mockUserRole(UserRole.ADMIN);
        Account account = new Account();

        // When
        boolean result = securityEvaluator.canCreateAccount(authentication, account);

        // Then
        assertTrue(result);
    }

    @Test
    void canCreateAccount_WithManager_ShouldReturnTrue() {
        // Given
        mockUserRole(UserRole.MANAGER);
        Account account = new Account();

        // When
        boolean result = securityEvaluator.canCreateAccount(authentication, account);

        // Then
        assertTrue(result);
    }

    @Test
    void canCreateAccount_WithSalespersonCreatingOwnAccount_ShouldReturnTrue() {
        // Given
        mockUserRole(UserRole.SALESPERSON);
        when(authentication.getName()).thenReturn(SALESPERSON_EMAIL);

        Account account = new Account();
        User user = new User();
        user.setEmail(SALESPERSON_EMAIL);
        account.setUser(user);

        // When
        boolean result = securityEvaluator.canCreateAccount(authentication, account);

        // Then
        assertTrue(result);
    }

    @Test
    void canCreateAccount_WithSalespersonCreatingOtherUserAccount_ShouldReturnFalse() {
        // Given
        mockUserRole(UserRole.SALESPERSON);
        when(authentication.getName()).thenReturn(SALESPERSON_EMAIL);

        Account account = new Account();
        User user = new User();
        user.setEmail("other@example.com");
        account.setUser(user);

        // When
        boolean result = securityEvaluator.canCreateAccount(authentication, account);

        // Then
        assertFalse(result);
    }

    @Test
    void canCreateAccount_WithSalespersonAndNullUser_ShouldReturnFalse() {
        // Given
        mockUserRole(UserRole.SALESPERSON);
        Account account = new Account();
        account.setUser(null);

        // When
        boolean result = securityEvaluator.canCreateAccount(authentication, account);

        // Then
        assertFalse(result);
    }

    @Test
    void canCreateAccount_WithNullAccount_ShouldReturnFalse() {
        // Given
        mockUserRole(UserRole.SALESPERSON);

        // When
        boolean result = securityEvaluator.canCreateAccount(authentication, null);

        // Then
        assertFalse(result);
    }

    private void mockUserRole(UserRole role) {
        doReturn(AuthorityUtils.createAuthorityList("ROLE_" + role.name()))
            .when(authentication).getAuthorities();
    }
}