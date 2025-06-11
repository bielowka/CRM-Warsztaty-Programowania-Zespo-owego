package com.customer.relationship.management.app.accounts;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateLeadStatusDTO {
    private LeadStatus status;
    private String reason;
}