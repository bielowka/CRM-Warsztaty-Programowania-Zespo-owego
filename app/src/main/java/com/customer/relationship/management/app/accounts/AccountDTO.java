package com.customer.relationship.management.app.accounts;

import lombok.Getter;

@Getter
public class AccountDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String companyName;

    public AccountDTO(Account account) {
        this.id = account.getId();
        this.firstName = account.getFirstName();
        this.lastName = account.getLastName();
        this.email = account.getEmail();
        this.phoneNumber = account.getPhoneNumber();
        this.companyName = account.getCompany() != null ? account.getCompany().getName() : null;
    }
}
