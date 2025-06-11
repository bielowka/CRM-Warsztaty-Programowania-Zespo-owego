package com.customer.relationship.management.app.sales;

import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Getter
@Setter
public class SalesRankingDTO {
    private Long userId;
    private String firstName;
    private String lastName;
    private BigDecimal totalAmount;
    private Long dealsCount;

    public SalesRankingDTO(
            Long userId,
            String firstName,
            String lastName,
            BigDecimal totalAmount,
            Long dealsCount
    ) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.totalAmount = totalAmount;
        this.dealsCount = dealsCount;
    }
}