package com.customer.relationship.management.app.accounts;

import com.customer.relationship.management.app.TestEntitiesUtils;
import com.customer.relationship.management.app.users.User;
import com.customer.relationship.management.app.users.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class AccountRepositoryIT {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UserRepository userRepository;

    private User testUser;

    @BeforeEach
    void setUp() {
        accountRepository.deleteAll();
        userRepository.deleteAll();

        testUser = TestEntitiesUtils.getTestUser("test@example.com");
        userRepository.save(testUser);
    }

    @AfterEach
    void tearDown() {
        accountRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void oneUserCanHaveMultipleAccounts() {
        // Given
        Account accountA = TestEntitiesUtils.getTestAccount(testUser, "accountA@a");
        Account accountB = TestEntitiesUtils.getTestAccount(testUser, "accountB@a");

        // When
        accountRepository.saveAll(List.of(accountA, accountB));

        // Then
        List<Account> accounts = accountRepository.findAllByUserId(testUser.getId());
        assertEquals(2, accounts.size());
        assertTrue(accounts.stream().allMatch(account -> account.getUser().getId().equals(testUser.getId())));
    }

    @Test
    void accountsAreReturnedByUser() {
        // Given
        User userA = TestEntitiesUtils.getTestUser("a@a");
        User userB = TestEntitiesUtils.getTestUser("b@b");
        userRepository.saveAll(List.of(userA, userB));

        Account accountA = TestEntitiesUtils.getTestAccount(userA, "accountA@a");
        Account accountB = TestEntitiesUtils.getTestAccount(userB, "accountB@b");
        accountRepository.saveAll(List.of(accountA, accountB));

        // When
        List<Account> accountsForUserA = accountRepository.findAllByUserId(userA.getId());

        // Then
        assertEquals(1, accountsForUserA.size());
        assertEquals(accountA.getEmail(), accountsForUserA.getFirst().getEmail());
    }

    @Test
    void findAllByAccountStatus_ShouldReturnMatchingAccounts() {
        // Given
        Account activeAccount = TestEntitiesUtils.getTestAccount(testUser, "active@example.com");
        activeAccount.setAccountStatus(AccountStatus.ACTIVE);

        Account inactiveAccount = TestEntitiesUtils.getTestAccount(testUser, "inactive@example.com");
        inactiveAccount.setAccountStatus(AccountStatus.INACTIVE);

        Account suspendedAccount = TestEntitiesUtils.getTestAccount(testUser, "suspended@example.com");
        suspendedAccount.setAccountStatus(AccountStatus.SUSPENDED);

        accountRepository.saveAll(List.of(activeAccount, inactiveAccount, suspendedAccount));

        // When
        List<Account> activeAccounts = accountRepository.findAllByAccountStatusAndUserId(AccountStatus.ACTIVE, testUser.getId());
        List<Account> inactiveAccounts = accountRepository.findAllByAccountStatusAndUserId(AccountStatus.INACTIVE, testUser.getId());
        List<Account> suspendedAccounts = accountRepository.findAllByAccountStatusAndUserId(AccountStatus.SUSPENDED, testUser.getId());

        // Then
        assertEquals(1, activeAccounts.size());
        assertEquals(activeAccount.getEmail(), activeAccounts.getFirst().getEmail());

        assertEquals(1, inactiveAccounts.size());
        assertEquals(inactiveAccount.getEmail(), inactiveAccounts.getFirst().getEmail());

        assertEquals(1, suspendedAccounts.size());
        assertEquals(suspendedAccount.getEmail(), suspendedAccounts.getFirst().getEmail());
    }
}
