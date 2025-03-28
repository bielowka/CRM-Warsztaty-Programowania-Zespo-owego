package com.customer.relationship.management.app;

import com.customer.relationship.management.app.accounts.Account;
import com.customer.relationship.management.app.accounts.AccountStatus;
import com.customer.relationship.management.app.users.User;
import com.customer.relationship.management.app.users.UserRole;

public final class TestEntitiesUtils {
    public static User getTestUser(String email) {
        User newUser = new User();
        newUser.setFirstName("John");
        newUser.setLastName("Doe");
        newUser.setEmail(email);
        newUser.setPassword("a");
        newUser.setRole(UserRole.SALESPERSON);
        return newUser;
    }

    public static Account getTestAccount(User user, String email) {
        Account accountA = new Account();
        accountA.setUser(user);
        accountA.setFirstName("John");
        accountA.setLastName("Doe");
        accountA.setEmail(email);
        accountA.setAccountStatus(AccountStatus.ACTIVE);
        accountA.setPhoneNumber("1234567890");
        return accountA;
    }
}
