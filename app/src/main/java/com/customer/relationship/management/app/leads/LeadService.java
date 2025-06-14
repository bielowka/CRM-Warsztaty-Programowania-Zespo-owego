package com.customer.relationship.management.app.leads;

import com.customer.relationship.management.app.accounts.Account;
import com.customer.relationship.management.app.accounts.AccountRepository;
import com.customer.relationship.management.app.sales.Sale;
import com.customer.relationship.management.app.sales.SaleRepository;
import com.customer.relationship.management.app.users.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class LeadService {

    private final LeadRepository leadRepository;
    private final AccountRepository accountRepository;
    private final SaleRepository saleRepository;

    public LeadService(LeadRepository leadRepository, AccountRepository accountRepository, SaleRepository saleRepository) {
        this.leadRepository = leadRepository;
        this.accountRepository = accountRepository;
        this.saleRepository = saleRepository;
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
    public LeadPageResponse getLeadsByUser(User user, String sortBy, String sortDirection, int page, int size) {
        Pageable pageable = createPageable(sortBy, sortDirection, page, size);
        Page<Lead> leadPage = leadRepository.findByAccountUser(user, pageable);
        return createLeadPageResponse(leadPage);
    }

    @Transactional(readOnly = true)
    public LeadPageResponse getAllLeads(String sortBy, String sortDirection, int page, int size) {
        Pageable pageable = createPageable(sortBy, sortDirection, page, size);
        Page<Lead> leadPage = leadRepository.findAll(pageable);
        return createLeadPageResponse(leadPage);
    }

    @Transactional
    public void updateStatus(Long leadId, LeadStatus newStatus) {
        Lead lead = leadRepository.findById(leadId)
                .orElseThrow(() -> new RuntimeException("Lead not found with id: " + leadId));

        lead.setStatus(newStatus);
        leadRepository.save(lead);
    }

    @Transactional
    public void closeLeadAndCreateSale(Long leadId, User currentUser) {
        Lead lead = leadRepository.findById(leadId)
                .orElseThrow(() -> new RuntimeException("Lead not found with id: " + leadId));

        if (lead.getStatus() != LeadStatus.CLOSED_WON) {
            throw new IllegalStateException("Can only create sale from a won lead");
        }

        Sale sale = new Sale();
        sale.setSalesRep(currentUser);
        sale.setAmount(lead.getEstimatedValue());
        sale.setCloseDate(LocalDateTime.now());
        saleRepository.save(sale);

        leadRepository.delete(lead);
    }

    private Pageable createPageable(String sortBy, String sortDirection, int page, int size) {
        Sort sort = createSort(sortBy, sortDirection);
        return PageRequest.of(page, size, sort);
    }

    private LeadPageResponse createLeadPageResponse(Page<Lead> leadPage) {
        List<LeadDTO> dtoList = leadPage.getContent().stream().map(lead -> {
            String companyName = null;
            String companyIndustry = null;

            if (lead.getAccount() != null && lead.getAccount().getCompany() != null) {
                companyName = lead.getAccount().getCompany().getName();
                companyIndustry = lead.getAccount().getCompany().getIndustry();
            }

            return new LeadDTO(
                    lead.getId(),
                    lead.getDescription(),
                    lead.getStatus(),
                    lead.getEstimatedValue(),
                    companyName,
                    companyIndustry,
                    lead.getCreatedAt(),
                    lead.getUpdatedAt()
            );
        }).toList();

        return new LeadPageResponse(
                dtoList,
                leadPage.getNumber(),
                leadPage.getSize(),
                leadPage.getTotalElements(),
                leadPage.getTotalPages()
        );
    }

    private Sort createSort(String sortBy, String sortDirection) {
        String mappedSortBy = mapSortField(sortBy);
        
        Sort.Direction direction = "desc".equalsIgnoreCase(sortDirection) 
            ? Sort.Direction.DESC 
            : Sort.Direction.ASC;
            
        return Sort.by(direction, mappedSortBy);
    }

    private String mapSortField(String sortBy) {
        return switch (sortBy.toLowerCase()) {
            case "createdat", "created_at", "created" -> "createdAt";
            case "updatedat", "updated_at", "updated" -> "updatedAt";
            case "description" -> "description";
            case "status" -> "status";
            case "estimatedvalue", "estimated_value", "value" -> "estimatedValue";
            default -> "id";
        };
    }
}