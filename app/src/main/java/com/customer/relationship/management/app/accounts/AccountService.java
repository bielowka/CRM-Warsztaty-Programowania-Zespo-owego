package com.customer.relationship.management.app.accounts;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AccountService {

    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public List<Account> findAll() {
        return accountRepository.findAll();
    }

    public Optional<Account> findById(Long id) {
        return accountRepository.findById(id);
    }

    public Account save(Account account) {
        if (account.getUser() == null) {
            throw new IllegalArgumentException("Account must have an associated user");
        }
        return accountRepository.save(account);
    }

    public void deleteById(Long id) {
        accountRepository.deleteById(id);
    }

    public List<Account> findAllByUserId(Long userId) {
        return accountRepository.findAllByUserId(userId);
    }

    public List<Account> findAllByAccountStatusAndUserId(AccountStatus accountStatus, Long userId) {
        return accountRepository.findAllByAccountStatusAndUserId(accountStatus, userId);
    }
}