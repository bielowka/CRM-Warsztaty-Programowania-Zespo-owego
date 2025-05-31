package com.customer.relationship.management.app.accounts;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
@CrossOrigin(origins = "http://localhost:5173")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public List<Account> getAllAccounts() {
        return accountService.findAll();
    }

    @GetMapping("/{id}")
    @PreAuthorize("@accountSecurity.canAccessAccount(authentication, #id)")
    public ResponseEntity<Account> getAccountById(@PathVariable Long id) {
        return accountService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("@accountSecurity.canAccessUserAccounts(authentication, #userId)")
    public List<Account> getAccountsByUserId(@PathVariable Long userId) {
        return accountService.findAllByUserId(userId);
    }

    @GetMapping("/user/{userId}/status/{status}")
    @PreAuthorize("@accountSecurity.canAccessUserAccounts(authentication, #userId)")
    public List<Account> getAccountsByUserIdAndStatus(
            @PathVariable Long userId,
            @PathVariable AccountStatus status) {
        return accountService.findAllByAccountStatusAndUserId(status, userId);
    }

    @PostMapping
    @PreAuthorize("@accountSecurity.canCreateAccount(authentication, #account)")
    public ResponseEntity<Account> createAccount(@Valid @RequestBody Account account) {
        Account savedAccount = accountService.save(account);
        return new ResponseEntity<>(savedAccount, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("@accountSecurity.canAccessAccount(authentication, #id)")
    public ResponseEntity<Account> updateAccount(@PathVariable Long id, @Valid @RequestBody Account updatedAccount) {
        return accountService.findById(id)
                .map(existing -> {
                    updatedAccount.setId(id);
                    return ResponseEntity.ok(accountService.save(updatedAccount));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<Void> deleteAccount(@PathVariable Long id) {
        if (accountService.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        accountService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}