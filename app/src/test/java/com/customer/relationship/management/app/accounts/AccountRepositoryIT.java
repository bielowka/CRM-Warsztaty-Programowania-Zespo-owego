package com.customer.relationship.management.app.accounts;

import com.customer.relationship.management.app.users.User;
import com.customer.relationship.management.app.users.UserRepository;
import com.customer.relationship.management.app.users.UsersFixture;
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
        User user = UsersFixture.getTestUser("a@a");
        Account accountA = AccountsFixture.getTestAccount(user, "accountA@a");
        Account accountB = AccountsFixture.getTestAccount(user, "accountB@a");
        user.setAccounts(List.of(accountA, accountB));

        // When
        userRepository.save(user);

        // Then
        List<Account> accounts = accountRepository.findAll();
        assertEquals(2, accounts.size());
        List<User> users = userRepository.findAll();
        assertEquals(1, users.size());
        assertEquals(2, users.getFirst().getAccounts().size());
    }

    @Test
    void accountsAreReturnedByUser() {
        // Given
        User userA = UsersFixture.getTestUser("a@a");
        User userB = UsersFixture.getTestUser("b@b");
        Account accountA = AccountsFixture.getTestAccount(userA, "accountA@a");
        Account accountB = AccountsFixture.getTestAccount(userB,"accountB@b");

        userA.setAccounts(List.of(accountA));
        userB.setAccounts(List.of(accountB));

        userRepository.saveAll(List.of(userA, userB));

        // When
        List<User> users = userRepository.findAll();

        // Then
        assertEquals(2, users.size());
        assertEquals(1, users.get(0).getAccounts().size());
        assertEquals(1, users.get(1).getAccounts().size());
    }

    @Test
    void canCreateAccountWithoutUser() {
        // Given
        Account account = AccountsFixture.getTestAccount(null, "accountA@a");

        // When
        accountRepository.save(account);

        // Then
        List<Account> accounts = accountRepository.findAll();
        assertEquals(1, accounts.size());
    }

    @Test
    void canChangeUserForAccount() {
        // Given
        User userA = UsersFixture.getTestUser("a@a");
        User userB = UsersFixture.getTestUser("b@b");
        Account account = AccountsFixture.getTestAccount(userA, "accountA@a");

        userRepository.saveAll(List.of(userA, userB));

        // When
        account.setUser(userB);
        accountRepository.save(account);

        // Then
        List<Account> accounts = accountRepository.findAll();
        assertEquals(1, accounts.size());
        assertEquals(userB.getEmail(), accounts.getFirst().getUser().getEmail());
    }
}
