package com.customer.relationship.management.app.accounts;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LeadService {

    private final LeadRepository leadRepository;

    public LeadService(LeadRepository leadRepository) {
        this.leadRepository = leadRepository;
    }

    @Transactional
    public Lead updateLeadStatus(Long leadId, UpdateLeadStatusDTO updateDTO) {
        Lead lead = leadRepository.findById(leadId)
                .orElseThrow(() -> new RuntimeException("Lead not found with id: " + leadId));

        // Możesz dodać logikę walidacji przejść między statusami
        validateStatusTransition(lead.getStatus(), updateDTO.getStatus());

        lead.setStatus(updateDTO.getStatus());
        return leadRepository.save(lead);
    }

    private void validateStatusTransition(LeadStatus currentStatus, LeadStatus newStatus) {
        // Przykładowa walidacja - blokuj bezpośrednie przejście z NEW na CLOSED
        if (currentStatus == LeadStatus.NEW &&
                (newStatus == LeadStatus.CLOSED_WON || newStatus == LeadStatus.CLOSED_LOST)) {
            throw new IllegalStateException("Cannot close a new lead without proper qualification");
        }
    }
}