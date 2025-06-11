package com.customer.relationship.management.app.accounts;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateLeadDTO {
    private String description;
    private LeadStatus status;
    private Double estimatedValue;
    private Integer probability;
    private Long accountId;
}
