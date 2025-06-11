package com.customer.relationship.management.app.teams;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reports")
@CrossOrigin(origins = "http://localhost:5173")
public class TeamReportController {

    private final TeamSalesReportService salesReportService;

    public TeamReportController(TeamSalesReportService salesReportService) {
        this.salesReportService = salesReportService;
    }

    @GetMapping("/team-sales")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<List<TeamSalesReportDTO>> getTeamSalesReport(
            @RequestParam int year,
            @RequestParam int month
    ) {
        List<TeamSalesReportDTO> report = salesReportService.getTeamSalesReport(year, month);
        return ResponseEntity.ok(report);
    }
}