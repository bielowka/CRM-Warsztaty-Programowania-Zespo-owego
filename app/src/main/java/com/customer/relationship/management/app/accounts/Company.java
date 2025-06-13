package com.customer.relationship.management.app.accounts;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "companies")
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String industry;

    public static Company of(String name, String industry) {
        Company company = new Company();
        company.name = name;
        company.industry = industry;
        return company;
    }
}