package com.customer.relationship.management.app.users;

import com.customer.relationship.management.app.TestEntitiesUtils;
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

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class UserControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserService userService;

    private User testAdmin;
    private User testManager;
    private User testSalesperson;

    @BeforeEach
    void setUp() {
        testAdmin = TestEntitiesUtils.getTestUser("admin@example.com");
        testAdmin.setRole(UserRole.ADMIN);
        testAdmin = userService.createUser(testAdmin);

        testManager = TestEntitiesUtils.getTestUser("manager@example.com");
        testManager.setRole(UserRole.MANAGER);
        testManager = userService.createUser(testManager);

        testSalesperson = TestEntitiesUtils.getTestUser("sales@example.com");
        testSalesperson.setRole(UserRole.SALESPERSON);
        testSalesperson = userService.createUser(testSalesperson);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getAllUsers_AsAdmin_ShouldReturnAllUsers() throws Exception {
        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(3))))
                .andExpect(jsonPath("$[*].email", hasItems(
                    testAdmin.getEmail(),
                    testManager.getEmail(),
                    testSalesperson.getEmail()
                )));
    }

    @Test
    @WithMockUser(roles = "SALESPERSON")
    void getAllUsers_AsSalesperson_ShouldBeForbidden() throws Exception {
        mockMvc.perform(get("/api/users"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getUserById_AsAdmin_ShouldReturnUser() throws Exception {
        mockMvc.perform(get("/api/users/{id}", testManager.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(testManager.getId().intValue())))
                .andExpect(jsonPath("$.email", is(testManager.getEmail())));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void createUser_AsAdmin_ShouldCreateUser() throws Exception {
        User newUser = TestEntitiesUtils.getTestUser("new@example.com");
        newUser.setRole(UserRole.SALESPERSON);

        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newUser)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email", is(newUser.getEmail())))
                .andExpect(jsonPath("$.role", is(newUser.getRole().name())));
    }

    @Test
    @WithMockUser(roles = "SALESPERSON")
    void createUser_AsSalesperson_ShouldBeForbidden() throws Exception {
        User newUser = TestEntitiesUtils.getTestUser("new@example.com");
        newUser.setRole(UserRole.SALESPERSON);

        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newUser)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updateUser_AsAdmin_ShouldUpdateUser() throws Exception {
        testSalesperson.setFirstName("Updated");
        testSalesperson.setLastName("Name");

        mockMvc.perform(put("/api/users/{id}", testSalesperson.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testSalesperson)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is("Updated")))
                .andExpect(jsonPath("$.lastName", is("Name")));
    }

    @Test
    @WithMockUser(roles = "SALESPERSON")
    void updateUser_AsSalesperson_ShouldBeForbidden() throws Exception {
        testSalesperson.setFirstName("Updated");

        mockMvc.perform(put("/api/users/{id}", testSalesperson.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testSalesperson)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteUser_AsAdmin_ShouldDeleteUser() throws Exception {
        mockMvc.perform(delete("/api/users/{id}", testSalesperson.getId()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/users/{id}", testSalesperson.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "SALESPERSON")
    void deleteUser_AsSalesperson_ShouldBeForbidden() throws Exception {
        mockMvc.perform(delete("/api/users/{id}", testSalesperson.getId()))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "sales@example.com", roles = "SALESPERSON")
    void changePassword_WithValidCurrentPassword_ShouldSucceed() throws Exception {
        ChangePasswordRequest request = new ChangePasswordRequest();
        request.setCurrentPassword("a");
        request.setNewPassword("newPassword123");

        mockMvc.perform(post("/api/users/change-password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "sales@example.com", roles = "SALESPERSON")
    void changePassword_WithInvalidCurrentPassword_ShouldReturnBadRequest() throws Exception {
        ChangePasswordRequest request = new ChangePasswordRequest();
        request.setCurrentPassword("wrongPassword");
        request.setNewPassword("newPassword123");

        mockMvc.perform(post("/api/users/change-password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }
} 