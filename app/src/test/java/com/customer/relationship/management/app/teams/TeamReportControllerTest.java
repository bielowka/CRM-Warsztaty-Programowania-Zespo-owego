package com.customer.relationship.management.app.teams;

import com.customer.relationship.management.app.sales.Sale;
import com.customer.relationship.management.app.sales.SaleRepository;
import com.customer.relationship.management.app.users.User;
import com.customer.relationship.management.app.users.UserService;
import com.customer.relationship.management.app.users.UserRole;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;



@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class TeamReportControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private SaleRepository saleRepository;

    @Autowired
    private UserService userService;

    private Team team;
    private User user;

    @BeforeEach
    void setUp() {
        team = new Team();
        team.setName("Reporting Team");
        team = teamRepository.save(team);

        user = new User();
        user.setFirstName("Report");
        user.setLastName("Tester");
        user.setEmail("reporter@example.com");
        user.setPassword("secret");
        user.setRole(UserRole.SALESPERSON);
        user.setTeam(team);
        user.setActive(true);
        user = userService.createUser(user);

        Sale sale = new Sale();
        sale.setSalesRep(user);
        sale.setAmount(BigDecimal.valueOf(10000));
        sale.setCloseDate(LocalDateTime.of(2025, 6, 5, 14, 30));
        saleRepository.save(sale);
    }

    @Test
    @WithMockUser(roles = "MANAGER")
    void getTeamSalesReport_AsManager_ShouldReturnReport() throws Exception {
        mockMvc.perform(get("/api/reports/team-sales")
                        .param("year", "2025")
                        .param("month", "6"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))))
                .andExpect(jsonPath("$[*].teamName", hasItem("Reporting Team")));
    }

    @Test
    @WithMockUser(roles = "SALESPERSON")
    void getTeamSalesReport_AsSalesperson_ShouldBeForbidden() throws Exception {
        mockMvc.perform(get("/api/reports/team-sales")
                        .param("year", "2025")
                        .param("month", "6"))
                .andExpect(status().isForbidden());
    }
}
