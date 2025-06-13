package com.customer.relationship.management.app.accounts;

import com.customer.relationship.management.app.teams.Team;
import com.customer.relationship.management.app.users.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AccountRepository extends JpaRepository<Account, Long> {

    List<AccountInfo> findAllBy();

    List<AccountInfo> findAllByUserId(Long userId);

    List<AccountInfo> findAllByAccountStatusAndUserId(AccountStatus accountStatus, Long userId);

    List<Account> findByUser(User user);

    @Query("SELECT a FROM Account a WHERE a.user.team = :team")
    List<Account> findByUserTeam(@Param("team") Team team);
}
