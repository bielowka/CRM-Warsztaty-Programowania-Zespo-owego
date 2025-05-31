package com.customer.relationship.management.app.config.security;

import com.customer.relationship.management.app.accounts.Account;
import com.customer.relationship.management.app.accounts.AccountService;
import com.customer.relationship.management.app.users.UserRole;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

@Component("accountSecurity")
public class AccountSecurityEvaluator {

    private final AccountService accountService;

    public AccountSecurityEvaluator(AccountService accountService) {
        this.accountService = accountService;
    }

    public boolean canCreateAccount(Authentication authentication, Account account) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        if (hasAnyRole(authentication, UserRole.ADMIN, UserRole.MANAGER)) {
            return true;
        }

        if (hasRole(authentication, UserRole.SALESPERSON)) {
            return account != null && 
                   account.getUser() != null && 
                   authentication.getName().equals(account.getUser().getEmail());
        }

        return false;
    }

    public boolean canAccessAccount(Authentication authentication, Long accountId) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        if (hasAnyRole(authentication, UserRole.ADMIN, UserRole.MANAGER)) {
            return true;
        }

        if (hasRole(authentication, UserRole.SALESPERSON)) {
            String userEmail = authentication.getName();
            Account account = accountService.findById(accountId).orElse(null);
            return account != null && account.getUser() != null && 
                   account.getUser().getEmail().equals(userEmail);
        }

        return false;
    }

    public boolean canAccessUserAccounts(Authentication authentication, Long userId) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        if (hasAnyRole(authentication, UserRole.ADMIN, UserRole.MANAGER)) {
            return true;
        }

        if (hasRole(authentication, UserRole.SALESPERSON)) {
            String userEmail = authentication.getName();
            return accountService.findAllByUserId(userId).stream()
                .findFirst()
                .map(account -> account.getUser().getEmail().equals(userEmail))
                .orElse(false);
        }

        return false;
    }

    private boolean hasRole(Authentication authentication, UserRole role) {
        return authentication.getAuthorities().contains(
            new SimpleGrantedAuthority("ROLE_" + role.name())
        );
    }

    private boolean hasAnyRole(Authentication authentication, UserRole... roles) {
        return java.util.Arrays.stream(roles)
            .anyMatch(role -> hasRole(authentication, role));
    }
} 