package com.customer.relationship.management.app.accounts;

import org.springframework.data.jpa.repository.JpaRepository;
import com.customer.relationship.management.app.users.User;

import java.util.List;

public interface AccountRepository extends JpaRepository<Account, Long> {

    List<AccountInfo> findAllBy();

    List<AccountInfo> findAllByUserId(Long userId);

    List<AccountInfo> findAllByAccountStatusAndUserId(AccountStatus accountStatus, Long userId);

    List<Account> findByUser(User user);
}
