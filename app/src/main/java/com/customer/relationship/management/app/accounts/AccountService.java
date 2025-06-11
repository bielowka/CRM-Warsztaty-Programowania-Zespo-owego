package com.customer.relationship.management.app.accounts;

import com.customer.relationship.management.app.users.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
class AccountService {

    private final AccountRepository accountRepository;

    public List<AccountInfo> findAll() {
        return accountRepository.findAllBy();
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

    public List<AccountInfo> findAllByUserId(Long userId) {
        return accountRepository.findAllByUserId(userId);
    }

    public List<AccountInfo> findAllByAccountStatusAndUserId(AccountStatus accountStatus, Long userId) {
        return accountRepository.findAllByAccountStatusAndUserId(accountStatus, userId);
    }
    public List<Account> getAccountsByUser(User user) {
        return accountRepository.findByUser(user);
    }
}