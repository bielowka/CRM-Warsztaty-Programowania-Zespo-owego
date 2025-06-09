package com.customer.relationship.management.app.accounts;

import com.customer.relationship.management.app.users.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class LeadService {

    private final LeadRepository leadRepository;
    private final AccountRepository accountRepository;

    public LeadService(LeadRepository leadRepository, AccountRepository accountRepository) {
        this.leadRepository = leadRepository;
        this.accountRepository = accountRepository;
    }

    @Transactional
    public Lead createLead(CreateLeadDTO createLeadDTO, User currentUser) {
        Account account = accountRepository.findById(createLeadDTO.getAccountId())
                .orElseThrow(() -> new RuntimeException("Account not found"));

        // Sprawdź czy konto należy do użytkownika
        if (!account.getUser().getId().equals(currentUser.getId())) {
            throw new SecurityException("You can only add leads to your own accounts");
        }

        Lead lead = new Lead();
        lead.setDescription(createLeadDTO.getDescription());
        lead.setStatus(createLeadDTO.getStatus() != null ? createLeadDTO.getStatus() : LeadStatus.NEW);
        lead.setEstimatedValue(BigDecimal.valueOf(createLeadDTO.getEstimatedValue()));
        lead.setAccount(account);

        return leadRepository.save(lead);
    }

    @Transactional(readOnly = true)
    public List<Lead> getLeadsByUser(User user) {
        return leadRepository.findByAccountUser(user);
    }

    @Transactional(readOnly = true)
    public List<Lead> getAllLeads() {
        return leadRepository.findAll();
    }

    @Transactional
    public void updateStatus(Long leadId, LeadStatus newStatus) {
        Lead lead = leadRepository.findById(leadId)
                .orElseThrow(() -> new RuntimeException("Lead not found with id: " + leadId));

        lead.setStatus(newStatus);
        leadRepository.save(lead);
    }

}