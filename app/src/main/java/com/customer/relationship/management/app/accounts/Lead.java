package com.customer.relationship.management.app.accounts;

import jakarta.persistence.*;

@Entity
@Table(name = "leads")
// TODO: extend in its own task
class Lead {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String description;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;
}