package com.customer.relationship.management.app.leads;


import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
public class LeadDTO {
    private final Long id;
    private final String description;
    private final LeadStatus status;
    private final BigDecimal estimatedValue;
    private final String companyName;
    private final String companyIndustry;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public LeadDTO(Long id, String description, LeadStatus status, BigDecimal estimatedValue,
                   String companyName, String companyIndustry, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.description = description;
        this.status = status;
        this.estimatedValue = estimatedValue;
        this.companyName = companyName;
        this.companyIndustry = companyIndustry;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
