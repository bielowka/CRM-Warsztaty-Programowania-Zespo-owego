package com.customer.relationship.management.app.accounts;

import jakarta.persistence.*;

@Entity
@Table(name = "companies")
class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String industry;

    static Company of(String name, String industry) {
        Company company = new Company();
        company.name = name;
        company.industry = industry;
        return company;
    }
}