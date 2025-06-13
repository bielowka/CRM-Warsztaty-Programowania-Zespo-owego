package com.customer.relationship.management.app.accounts;

import com.customer.relationship.management.app.users.User;
import com.customer.relationship.management.app.users.UserRepository;
import com.customer.relationship.management.app.users.UserRole;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

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
        LeadStatus status = LeadStatus.valueOf(newStatus);
        leadService.updateStatus(id, status);

        if (status == LeadStatus.CLOSED_WON) {
            String email = SecurityContextHolder.getContext().getAuthentication().getName();
            User currentUser = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            leadService.closeLeadAndCreateSale(id, currentUser);
        }
        
        return ResponseEntity.ok().build();
    }

    @GetMapping
    @PreAuthorize("hasRole('SALESPERSON') or hasRole('MANAGER')")
    public ResponseEntity<LeadPageResponse> getLeads(
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Authentication authentication) {
        String email = authentication.getName();
        User currentUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        LeadPageResponse response;

        if (currentUser.getRole() == UserRole.SALESPERSON) {
            response = leadService.getLeadsByUser(currentUser, sortBy, sortDirection, page, size);
        } else if (currentUser.getRole() == UserRole.MANAGER) {
            response = leadService.getAllLeads(sortBy, sortDirection, page, size);
        } else {
            return ResponseEntity.status(403).build();
        }

        return ResponseEntity.ok(response);
    }


}