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
}