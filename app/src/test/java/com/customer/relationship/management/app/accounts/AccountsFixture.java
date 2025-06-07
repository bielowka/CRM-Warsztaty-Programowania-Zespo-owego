package com.customer.relationship.management.app.accounts;

import com.customer.relationship.management.app.users.User;

final class AccountsFixture {
    static Account getTestAccount(User user, String email) {
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
