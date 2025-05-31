package com.customer.relationship.management.app.accounts;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountRepository extends JpaRepository<Account, Long> {

    List<Account> findAllByUserId(Long userId);

    List<Account> findAllByAccountStatusAndUserId(AccountStatus accountStatus, Long userId);
}
