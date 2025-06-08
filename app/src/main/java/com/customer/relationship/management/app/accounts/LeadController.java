package com.customer.relationship.management.app.accounts;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/leads")
public class LeadController {

    private final LeadService leadService;

    public LeadController(LeadService leadService) {
        this.leadService = leadService;
    }

    @PatchMapping("/{leadId}/status")
    public ResponseEntity<Lead> updateLeadStatus(
            @PathVariable Long leadId,
            @RequestBody UpdateLeadStatusDTO updateDTO
    ) {
        Lead updatedLead = leadService.updateLeadStatus(leadId, updateDTO);
        return ResponseEntity.ok(updatedLead);
    }
}