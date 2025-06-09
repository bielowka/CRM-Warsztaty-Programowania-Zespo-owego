package com.customer.relationship.management.app.accounts;


import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class LeadDTO {
    private Long id;
    private String description;
    private LeadStatus status;
    private BigDecimal estimatedValue;
    private String companyName;
    private String companyIndustry;

    public LeadDTO(Long id, String description, LeadStatus status, BigDecimal estimatedValue,
                   String companyName, String companyIndustry) {
        this.id = id;
        this.description = description;
        this.status = status;
        this.estimatedValue = estimatedValue;
        this.companyName = companyName;
        this.companyIndustry = companyIndustry;
    }
}
