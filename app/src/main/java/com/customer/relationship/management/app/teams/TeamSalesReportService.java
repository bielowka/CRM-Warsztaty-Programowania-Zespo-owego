package com.customer.relationship.management.app.teams;


import com.customer.relationship.management.app.sales.SaleRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TeamSalesReportService {

    private final SaleRepository saleRepository;

    public TeamSalesReportService(SaleRepository saleRepository) {
        this.saleRepository = saleRepository;
    }

    public List<TeamSalesReportDTO> getTeamSalesReport(int year, int month) {
        List<Object[]> results = saleRepository.findTeamSalesPerformance(year, month);

        return results.stream()
                .map(result -> new TeamSalesReportDTO(
                        (Long) result[0],       // teamId
                        (String) result[1],     // teamName
                        (BigDecimal) result[2], // totalSales
                        (Long) result[3],      // dealsCount
                        (Long) result[4]        // teamSize
                ))
                .sorted((t1, t2) -> t2.getTotalSales().compareTo(t1.getTotalSales()))
                .collect(Collectors.toList());
    }
}