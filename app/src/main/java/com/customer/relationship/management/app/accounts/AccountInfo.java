package com.customer.relationship.management.app.accounts;

interface AccountInfo {
    Long getId();
    String getFirstName();
    String getLastName();
    String getEmail();
    AccountStatus getAccountStatus();
    String getPhoneNumber();
    String getCompanyName();
}
