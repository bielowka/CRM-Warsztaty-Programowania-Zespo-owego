package com.customer.relationship.management.app.teams;

import com.customer.relationship.management.app.sales.SaleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class TeamSalesReportServiceTest {

    private SaleRepository saleRepository;
    private TeamSalesReportService reportService;

    private Object[] rowTeamA;
    private Object[] rowTeamB;

    @BeforeEach
    void setUp() {
        saleRepository = mock(SaleRepository.class);
        reportService = new TeamSalesReportService(saleRepository);

        rowTeamA = new Object[]{1L, "Team A", BigDecimal.valueOf(15000), 3L, 2L};
        rowTeamB = new Object[]{2L, "Team B", BigDecimal.valueOf(10000), 2L, 3L};
    }

    @Test
    void getTeamSalesReport_ShouldReturnSortedReportByTotalSales() {
        // given
        when(saleRepository.findTeamSalesPerformance(2025, 6))
                .thenReturn(List.of(rowTeamA, rowTeamB));

        // when
        List<TeamSalesReportDTO> report = reportService.getTeamSalesReport(2025, 6);

        // then
        assertThat(report)
                .hasSize(2)
                .extracting(TeamSalesReportDTO::getTeamName)
                .containsExactly("Team A", "Team B"); // posortowane malejąco po totalSales

        verify(saleRepository).findTeamSalesPerformance(2025, 6);
    }

    @Test
    void getTeamSalesReport_WhenNoResults_ShouldReturnEmptyList() {
        // given
        when(saleRepository.findTeamSalesPerformance(2025, 7)).thenReturn(List.of());

        // when
        List<TeamSalesReportDTO> report = reportService.getTeamSalesReport(2025, 7);

        // then
        assertThat(report).isEmpty();
        verify(saleRepository).findTeamSalesPerformance(2025, 7);
    }
}
