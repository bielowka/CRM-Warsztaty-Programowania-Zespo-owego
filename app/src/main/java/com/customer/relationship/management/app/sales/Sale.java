package com.customer.relationship.management.app.sales;

import com.customer.relationship.management.app.accounts.Account;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "sales")
@Getter
@Setter
public class Sale {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "sales_rep_id", nullable = false)
    private Account salesRep;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(name = "close_date", nullable = false)
    private LocalDateTime closeDate;
}

