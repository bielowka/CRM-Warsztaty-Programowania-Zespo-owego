package com.customer.relationship.management.app.teams;

import com.customer.relationship.management.app.users.User;
import com.customer.relationship.management.app.users.UserRole;
import com.customer.relationship.management.app.users.UserService;
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

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class TeamControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private UserService userService;

    private Team testTeam;

    @BeforeEach
    void setUp() {
        Team team = new Team();
        team.setName("Test Team");
        testTeam = teamRepository.save(team);
    }

    @Test
    @WithMockUser(roles = "MANAGER")
    void getAllTeams_AsManager_ShouldReturnTeamList() throws Exception {
        mockMvc.perform(get("/api/teams"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))))
                .andExpect(jsonPath("$[*].name", hasItem("Test Team")));
    }

    @Test
    @WithMockUser(roles = "SALESPERSON")
    void getAllTeams_AsSalesperson_ShouldBeForbidden() throws Exception {
        mockMvc.perform(get("/api/teams"))
                .andExpect(status().isForbidden());
    }
}
