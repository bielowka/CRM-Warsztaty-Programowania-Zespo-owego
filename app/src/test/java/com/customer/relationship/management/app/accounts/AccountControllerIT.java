package com.customer.relationship.management.app.accounts;

import com.customer.relationship.management.app.TestEntitiesUtils;
import com.customer.relationship.management.app.users.User;
import com.customer.relationship.management.app.users.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class AccountControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AccountService accountService;

    @Autowired
    private UserService userService;

    private Account testAccount1;
    private Account testAccount2;
    private Account testAccount3;
    private Account testAccount4;
    private Account testAccount5;

    private CreateAccountDTO createAccountDTO;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = TestEntitiesUtils.getTestUser("test@example.com");
        User otherUser = TestEntitiesUtils.getTestUser("other@example.com");
        testUser = userService.createUser(testUser);
        otherUser = userService.createUser(otherUser);
        
        testAccount1 = AccountsFixture.getTestAccount(testUser, "account1@example.com");
        testAccount2 = AccountsFixture.getTestAccount(testUser, "account2@example.com");
        testAccount3 = AccountsFixture.getTestAccount(testUser, "account3@example.com");
        testAccount4 = AccountsFixture.getTestAccount(testUser, "account4@example.com");
        testAccount5 = AccountsFixture.getTestAccount(otherUser, "account5@example.com");

        createAccountDTO = new CreateAccountDTO();
        createAccountDTO.setFirstName(testAccount1.getFirstName());
        createAccountDTO.setLastName(testAccount1.getLastName());
        createAccountDTO.setEmail(testAccount1.getEmail());
        createAccountDTO.setPhoneNumber(testAccount1.getPhoneNumber());
        createAccountDTO.setAccountStatus(testAccount1.getAccountStatus());
        createAccountDTO.setCompanyName("companyName");
        createAccountDTO.setIndustry("industry");
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getAllAccounts_AsAdmin_ShouldReturnAllAccounts() throws Exception {
        saveAllAccounts();

        mockMvc.perform(get("/api/accounts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(5))))
                .andExpect(jsonPath("$[*].email", hasItems(
                    testAccount1.getEmail(),
                    testAccount2.getEmail(),
                    testAccount3.getEmail(),
                    testAccount4.getEmail(),
                    testAccount5.getEmail()
                )));
    }

    private void saveAllAccounts() {
        accountService.save(testAccount1);
        accountService.save(testAccount2);
        accountService.save(testAccount3);
        accountService.save(testAccount4);
        accountService.save(testAccount5);
    }

    @Test
    @WithMockUser(roles = "SALESPERSON")
    void getAllAccounts_AsSalesperson_ShouldBeForbidden() throws Exception {
        mockMvc.perform(get("/api/accounts"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getAccountById_AsAdmin_ShouldReturnAccount() throws Exception {
        Account savedAccount = accountService.save(testAccount1);

        mockMvc.perform(get("/api/accounts/{id}", savedAccount.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(savedAccount.getId().intValue())))
                .andExpect(jsonPath("$.email", is(savedAccount.getEmail())));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getAccountById_WithNonExistentId_ShouldReturn404() throws Exception {
        mockMvc.perform(get("/api/accounts/{id}", 999))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "test@example.com", roles = "SALESPERSON")
    void getAccountById_AsSalespersonOwningAccount_ShouldReturnAccount() throws Exception {
        Account savedAccount = accountService.save(testAccount1);

        mockMvc.perform(get("/api/accounts/{id}", savedAccount.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(savedAccount.getId().intValue())));
    }

    @Test
    @WithMockUser(username = "other@example.com", roles = "SALESPERSON")
    void getAccountById_AsSalespersonNotOwningAccount_ShouldBeForbidden() throws Exception {
        Account savedAccount = accountService.save(testAccount1);

        mockMvc.perform(get("/api/accounts/{id}", savedAccount.getId()))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "test@example.com", roles = "SALESPERSON")
    void createAccount_AsSalespersonForOwnAccount_ShouldCreateAccount() throws Exception {
        mockMvc.perform(post("/api/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createAccountDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email", is(testAccount1.getEmail())));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updateAccount_AsAdmin_ShouldUpdateAccount() throws Exception {
        Account savedAccount = accountService.save(testAccount1);
        UpdateAccountDTO updateDto = getUpdateAccountDto(savedAccount);

        mockMvc.perform(put("/api/accounts/{id}", savedAccount.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is("Updated")));
    }

    private static UpdateAccountDTO getUpdateAccountDto(Account savedAccount) {
        UpdateAccountDTO updateAccountDTO = new UpdateAccountDTO();
        updateAccountDTO.setFirstName("Updated");
        updateAccountDTO.setLastName(savedAccount.getLastName());
        updateAccountDTO.setEmail(savedAccount.getEmail());
        updateAccountDTO.setPhoneNumber(savedAccount.getPhoneNumber());
        updateAccountDTO.setAccountStatus(AccountStatus.INACTIVE);
        return updateAccountDTO;
    }

    @Test
    @WithMockUser(username = "test@example.com", roles = "SALESPERSON")
    void updateAccount_AsSalespersonOwningAccount_ShouldUpdateAccount() throws Exception {
        Account savedAccount = accountService.save(testAccount1);
        UpdateAccountDTO updateDto = getUpdateAccountDto(savedAccount);

        mockMvc.perform(put("/api/accounts/{id}", savedAccount.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is("Updated")));
    }

    @Test
    @WithMockUser(username = "other@example.com", roles = "SALESPERSON")
    void updateAccount_AsSalespersonNotOwningAccount_ShouldBeForbidden() throws Exception {
        Account savedAccount = accountService.save(testAccount1);
        UpdateAccountDTO updateDto = getUpdateAccountDto(savedAccount);

        mockMvc.perform(put("/api/accounts/{id}", savedAccount.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteAccount_AsAdmin_ShouldDeleteAccount() throws Exception {
        Account savedAccount = accountService.save(testAccount1);

        mockMvc.perform(delete("/api/accounts/{id}", savedAccount.getId()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/accounts/{id}", savedAccount.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "SALESPERSON")
    void deleteAccount_AsSalesperson_ShouldBeForbidden() throws Exception {
        Account savedAccount = accountService.save(testAccount1);

        mockMvc.perform(delete("/api/accounts/{id}", savedAccount.getId()))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getAccountsByUserId_AsAdmin_ShouldReturnAccounts() throws Exception {
        saveAllAccounts();

        mockMvc.perform(get("/api/accounts/user/{userId}", testUser.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(4)))
                .andExpect(jsonPath("$[*].email", hasItems(
                    testAccount1.getEmail(),
                    testAccount2.getEmail(),
                    testAccount3.getEmail(),
                    testAccount4.getEmail()
                )));
    }

    @Test
    @WithMockUser(username = "test@example.com", roles = "SALESPERSON")
    void getAccountsByUserId_AsSalespersonForOwnAccounts_ShouldReturnAccounts() throws Exception {
        saveAllAccounts();

        mockMvc.perform(get("/api/accounts/user/{userId}", testUser.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(4)))
                .andExpect(jsonPath("$[*].email", hasItems(
                    testAccount1.getEmail(),
                    testAccount2.getEmail(),
                    testAccount3.getEmail(),
                    testAccount4.getEmail()
                )));
    }

    @Test
    @WithMockUser(username = "other@example.com", roles = "SALESPERSON")
    void getAccountsByUserId_AsSalespersonForOtherUserAccounts_ShouldBeForbidden() throws Exception {
        saveAllAccounts();

        mockMvc.perform(get("/api/accounts/user/{userId}", testUser.getId()))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getAccountsByUserIdAndStatus_AsAdmin_ShouldReturnFilteredAccounts() throws Exception {
        testAccount1.setAccountStatus(AccountStatus.ACTIVE);
        testAccount2.setAccountStatus(AccountStatus.INACTIVE);
        testAccount3.setAccountStatus(AccountStatus.SUSPENDED);
        testAccount4.setAccountStatus(AccountStatus.ACTIVE);

        saveAllAccounts();

        mockMvc.perform(get("/api/accounts/user/{userId}/status/{status}", 
                testUser.getId(), AccountStatus.ACTIVE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[*].email", hasItems(
                    testAccount1.getEmail(), testAccount4.getEmail()
                )));

        mockMvc.perform(get("/api/accounts/user/{userId}/status/{status}", 
                testUser.getId(), AccountStatus.INACTIVE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].email", is(testAccount2.getEmail())));

        mockMvc.perform(get("/api/accounts/user/{userId}/status/{status}", 
                testUser.getId(), AccountStatus.SUSPENDED))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].email", is(testAccount3.getEmail())));
    }
}
