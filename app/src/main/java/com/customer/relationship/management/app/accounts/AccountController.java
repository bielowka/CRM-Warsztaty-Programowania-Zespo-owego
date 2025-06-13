package com.customer.relationship.management.app.accounts;

import com.customer.relationship.management.app.users.User;
import com.customer.relationship.management.app.users.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/accounts")
@CrossOrigin(origins = "http://localhost:5173")
@RequiredArgsConstructor
class AccountController {

    private final AccountService accountService;
    private final UserRepository userRepository;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public List<AccountInfo> getAllAccounts() {
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
    public List<AccountInfo> getAccountsByUserId(@PathVariable Long userId) {
        return accountService.findAllByUserId(userId);
    }

    @GetMapping("/user/{userId}/status/{status}")
    @PreAuthorize("@accountSecurity.canAccessUserAccounts(authentication, #userId)")
    public List<AccountInfo> getAccountsByUserIdAndStatus(
            @PathVariable Long userId,
            @PathVariable AccountStatus status) {
        return accountService.findAllByAccountStatusAndUserId(status, userId);
    }

    @PostMapping
    @PreAuthorize("hasRole('SALESPERSON')")
    public ResponseEntity<Account> createAccount(@Valid @RequestBody CreateAccountDTO createAccountDTO) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return ResponseEntity.ok(accountService.createAccount(createAccountDTO, user));
    }

    @PutMapping("/{id}")
    @PreAuthorize("@accountSecurity.canAccessAccount(authentication, #id)")
    public ResponseEntity<Account> updateAccount(@PathVariable Long id, @Valid @RequestBody UpdateAccountDTO updateAccountDTO) {
        Optional<Account> existingAccount = accountService.findById(id);
        return existingAccount.map(account -> ResponseEntity.ok(accountService.updateAccount(account, updateAccountDTO)))
                .orElseGet(() -> ResponseEntity.notFound().build());
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

    @GetMapping("/my")
    @PreAuthorize("hasRole('SALESPERSON')")
    public ResponseEntity<List<AccountDTO>> getMyAccounts(Authentication authentication) {
        String email = authentication.getName();
        User currentUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Account> accounts = accountService.getAccountsByUser(currentUser);
        List<AccountDTO> dtos = accounts.stream()
                .map(AccountDTO::new)
                .toList();

        return ResponseEntity.ok(dtos);
    }
}