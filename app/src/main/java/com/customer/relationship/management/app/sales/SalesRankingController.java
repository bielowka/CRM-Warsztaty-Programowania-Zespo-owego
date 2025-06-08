package com.customer.relationship.management.app.sales;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ranking")
@CrossOrigin(origins = "http://localhost:5173")
public class SalesRankingController {

    private final SalesRankingService salesRankingService;

    public SalesRankingController(SalesRankingService salesRankingService) {
        this.salesRankingService = salesRankingService;
    }

    @GetMapping("/sales")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<List<SalesRankingDTO>> getSalesRanking(
            @RequestParam int year,
            @RequestParam int month
    ) {
        List<SalesRankingDTO> ranking = salesRankingService.getSalesRanking(year, month);
        return ResponseEntity.ok(ranking);
    }
}