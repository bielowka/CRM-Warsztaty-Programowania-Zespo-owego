package com.customer.relationship.management.app.accounts;

import jakarta.persistence.*;

@Entity
@Table(name = "notes")
// TODO: extend in its own task
public class Note {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String content;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;
}