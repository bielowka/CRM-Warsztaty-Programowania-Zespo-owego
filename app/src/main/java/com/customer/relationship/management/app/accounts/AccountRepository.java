package com.customer.relationship.management.app.accounts;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

interface AccountRepository extends JpaRepository<Account, Long> {

    List<AccountInfo> findAllBy();

    List<AccountInfo> findAllByUserId(Long userId);

    List<AccountInfo> findAllByAccountStatusAndUserId(AccountStatus accountStatus, Long userId);
}
