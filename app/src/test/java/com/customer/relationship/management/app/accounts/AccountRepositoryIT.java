package com.customer.relationship.management.app.accounts;

import com.customer.relationship.management.app.TestEntitiesUtils;
import com.customer.relationship.management.app.users.User;
import com.customer.relationship.management.app.users.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class AccountRepositoryIT {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        accountRepository.deleteAll();
        userRepository.deleteAll();
    }

    @AfterEach
    void tearDown() {
        accountRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void oneUserCanHaveMultipleAccounts() {
        // Given
        User user = TestEntitiesUtils.getTestUser("a@a");
        userRepository.save(user);

        Account accountA = TestEntitiesUtils.getTestAccount(user, "accountA@a");
        Account accountB = TestEntitiesUtils.getTestAccount(user, "accountB@a");

        // When
        accountRepository.saveAll(List.of(accountA, accountB));

        // Then
        List<Account> accounts = accountRepository.findAll();
        assertEquals(2, accounts.size());
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
        List<User> users = userRepository.findAll();
        List<Account> accounts = accountRepository.findAllByUserId(userA.getId());

        // Then
        assertEquals(2, users.size());
        assertEquals(1, accounts.size());
    }
}
