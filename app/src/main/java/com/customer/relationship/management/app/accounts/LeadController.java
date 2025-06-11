package com.customer.relationship.management.app.accounts;

import com.customer.relationship.management.app.users.User;
import com.customer.relationship.management.app.users.UserRepository;
import com.customer.relationship.management.app.users.UserRole;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/leads")
public class LeadController {

    private final LeadService leadService;
    private final UserRepository userRepository;

    public LeadController(LeadService leadService, UserRepository userRepository) {
        this.leadService = leadService;
        this.userRepository = userRepository;
    }

    @PostMapping
    @PreAuthorize("hasRole('SALESPERSON')")
    public ResponseEntity<Lead> createLead(
            @RequestBody CreateLeadDTO createLeadDTO,
            Authentication authentication
    ) {
        String email = authentication.getName();
        User currentUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Lead createdLead = leadService.createLead(createLeadDTO, currentUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdLead);
    }

    @PostMapping("/{id}/status")
    @PreAuthorize("hasRole('SALESPERSON')")
    public ResponseEntity<Void> updateLeadStatus(@PathVariable Long id, @RequestBody Map<String, String> body) {
        String newStatus = body.get("status");
        leadService.updateStatus(id, LeadStatus.valueOf(newStatus));
        return ResponseEntity.ok().build();
    }

    @GetMapping
    @PreAuthorize("hasRole('SALESPERSON') or hasRole('MANAGER')")
    public ResponseEntity<List<LeadDTO>> getLeads(Authentication authentication) {
        String email = authentication.getName();
        User currentUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Lead> leads;

        if (currentUser.getRole() == UserRole.SALESPERSON) {
            leads = leadService.getLeadsByUser(currentUser);
        } else if (currentUser.getRole() == UserRole.MANAGER) {
            leads = leadService.getAllLeads();
        } else {
            return ResponseEntity.status(403).build();
        }

        List<LeadDTO> dtoList = leads.stream().map(lead -> {
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
                    companyIndustry
            );
        }).toList();

        return ResponseEntity.ok(dtoList);
    }


}