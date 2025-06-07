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
    private CompanyRepository companyRepository;

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
        Account accountA = AccountsFixture.getTestAccount(testUser, "accountA@a");
        Account accountB = AccountsFixture.getTestAccount(testUser, "accountB@a");

        // When
        accountRepository.saveAll(List.of(accountA, accountB));

        // Then
        List<AccountInfo> accounts = accountRepository.findAllByUserId(testUser.getId());
        assertEquals(2, accounts.size());
        assertTrue(accounts.stream()
                .allMatch(accountInfo -> {
                        Account account = accountRepository.findById(accountInfo.getId()).orElseThrow();
                        return account.getUser().getId().equals(testUser.getId());
        }));
    }

    @Test
    void accountsAreReturnedByUser() {
        // Given
        User userA = TestEntitiesUtils.getTestUser("a@a");
        User userB = TestEntitiesUtils.getTestUser("b@b");
        userRepository.saveAll(List.of(userA, userB));

        Account accountA = AccountsFixture.getTestAccount(userA, "accountA@a");
        Account accountB = AccountsFixture.getTestAccount(userB, "accountB@b");
        accountRepository.saveAll(List.of(accountA, accountB));

        // When
        List<AccountInfo> accountsForUserA = accountRepository.findAllByUserId(userA.getId());

        // Then
        assertEquals(1, accountsForUserA.size());
        assertEquals(accountA.getEmail(), accountsForUserA.getFirst().getEmail());
    }

    @Test
    void findAllByAccountStatus_ShouldReturnMatchingAccounts() {
        // Given
        Account activeAccount = AccountsFixture.getTestAccount(testUser, "active@example.com");
        activeAccount.setAccountStatus(AccountStatus.ACTIVE);

        Account inactiveAccount = AccountsFixture.getTestAccount(testUser, "inactive@example.com");
        inactiveAccount.setAccountStatus(AccountStatus.INACTIVE);

        Account suspendedAccount = AccountsFixture.getTestAccount(testUser, "suspended@example.com");
        suspendedAccount.setAccountStatus(AccountStatus.SUSPENDED);

        accountRepository.saveAll(List.of(activeAccount, inactiveAccount, suspendedAccount));

        // When
        List<AccountInfo> activeAccounts = accountRepository.findAllByAccountStatusAndUserId(AccountStatus.ACTIVE, testUser.getId());
        List<AccountInfo> inactiveAccounts = accountRepository.findAllByAccountStatusAndUserId(AccountStatus.INACTIVE, testUser.getId());
        List<AccountInfo> suspendedAccounts = accountRepository.findAllByAccountStatusAndUserId(AccountStatus.SUSPENDED, testUser.getId());

        // Then
        assertEquals(1, activeAccounts.size());
        assertEquals(activeAccount.getEmail(), activeAccounts.getFirst().getEmail());

        assertEquals(1, inactiveAccounts.size());
        assertEquals(inactiveAccount.getEmail(), inactiveAccounts.getFirst().getEmail());

        assertEquals(1, suspendedAccounts.size());
        assertEquals(suspendedAccount.getEmail(), suspendedAccounts.getFirst().getEmail());
    }

    @Test
    void findAllBy_projectionCorrectlyReturnsCompanyName() {
        // Given
        Company company = Company.of("ABC Corp", "123456789");
        Account account = AccountsFixture.getTestAccount(testUser, "abccorp@example.com");
        account.setCompany(company);

        companyRepository.save(company);
        accountRepository.save(account);

        // When
        List<AccountInfo> accounts = accountRepository.findAllByUserId(testUser.getId());

        // Then
        assertEquals("ABC Corp", accounts.getFirst().getCompanyName());
    }
}
