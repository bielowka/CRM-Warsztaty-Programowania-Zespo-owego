package com.customer.relationship.management.app.teams;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class TeamSalesReportDTO {
    private Long teamId;
    private String teamName;
    private BigDecimal totalSales;
    private Long dealsCount;
    private Long teamSize;

    public TeamSalesReportDTO(
            Long teamId,
            String teamName,
            BigDecimal totalSales,
            Long dealsCount,
            Long teamSize
    ) {
        this.teamId = teamId;
        this.teamName = teamName;
        this.totalSales = totalSales;
        this.dealsCount = dealsCount;
        this.teamSize = teamSize;
    }
}