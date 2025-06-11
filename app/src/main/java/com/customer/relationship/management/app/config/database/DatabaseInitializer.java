package com.customer.relationship.management.app.config.database;

import com.customer.relationship.management.app.accounts.*;
import com.customer.relationship.management.app.sales.Sale;
import com.customer.relationship.management.app.sales.SaleRepository;
import com.customer.relationship.management.app.teams.Team;
import com.customer.relationship.management.app.teams.TeamRepository;
import com.customer.relationship.management.app.users.User;
import com.customer.relationship.management.app.users.UserRepository;
import com.customer.relationship.management.app.users.UserRole;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Component
@Profile("!test")
public class DatabaseInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TeamRepository teamRepository;
    private final AccountRepository accountRepository;
    private final SaleRepository saleRepository;
    private final LeadRepository leadRepository;
    private final CompanyRepository companyRepository;

    public DatabaseInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder, TeamRepository teamRepository,
                               AccountRepository accountRepository, SaleRepository saleRepository, LeadRepository leadRepository, CompanyRepository companyRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.teamRepository = teamRepository;
        this.accountRepository = accountRepository;
        this.saleRepository = saleRepository;
        this.leadRepository = leadRepository;
        this.companyRepository = companyRepository;

    }

    @Override
    public void run(String... args) {

        List<User> users = List.of(
                createUser("Jan", "Kowalski", "jan@example.com", "password123", UserRole.ADMIN, "CEO"),
                createUser("Anna", "Nowak", "anna@example.com", "password123", UserRole.SALESPERSON, "Sales"),
                createUser("Piotr", "Zieliński", "piotr@example.com", "password123", UserRole.SALESPERSON, "Sales"),
                createUser("Kasia", "Wiśniewska", "kasia@example.com", "password123", UserRole.MANAGER, "Marketing Manager"),
                createUser("Tomek", "Lis", "tomek@example.com", "password123", UserRole.SALESPERSON, "IT"),
                createUser("Ewa", "Malinowska", "ewa@example.com", "password123", UserRole.SALESPERSON, "HR"),
                createUser("Marek", "Mazur", "marek@example.com", "password123", UserRole.SALESPERSON, "Logistics"),
                createUser("Natalia", "Kaczmarek", "natalia@example.com", "password123", UserRole.SALESPERSON, "Legal"),
                createUser("Paweł", "Wójcik", "pawel@example.com", "password123", UserRole.SALESPERSON, "Finance"),
                createUser("Zofia", "Dąbrowska", "zofia@example.com", "password123", UserRole.SALESPERSON, "Design")
        );

        User salesRep1 = createUser("Mike", "Johnson", "sales1@crm.com", "sales1", UserRole.SALESPERSON, "Sales");
        User salesRep2 = createUser("Sarah", "Williams", "sales2@crm.com", "sales2", UserRole.SALESPERSON, "Sales");
        User salesRep3 = createUser("Tom", "Brown", "sales3@crm.com", "sales3", UserRole.SALESPERSON, "Sales");
        User salesRep4 = createUser("Emily", "Davis", "sales4@crm.com", passwordEncoder.encode("sales4"), UserRole.SALESPERSON, "Sales");
        User salesRep5 = createUser("Daniel", "Miller", "sales5@crm.com", passwordEncoder.encode("sales5"), UserRole.SALESPERSON, "Sales");
        User salesRep6 = createUser("Olivia", "Garcia", "sales6@crm.com", passwordEncoder.encode("sales6"), UserRole.SALESPERSON, "Sales");
        User salesRep7 = createUser("David", "Martinez", "sales7@crm.com", passwordEncoder.encode("sales7"), UserRole.SALESPERSON, "Sales");
        User salesRep8 = createUser("Emma", "Rodriguez", "sales8@crm.com", passwordEncoder.encode("sales8"), UserRole.SALESPERSON, "Sales");
        User salesRep9 = createUser("James", "Lee", "sales9@crm.com", passwordEncoder.encode("sales9"), UserRole.SALESPERSON, "Sales");
        User salesRep10 = createUser("Sophia", "Walker", "sales10@crm.com", passwordEncoder.encode("sales10"), UserRole.SALESPERSON, "Sales");
        User salesRep11 = createUser("Ethan", "Hernandez", "sales11@crm.com", passwordEncoder.encode("sales11"), UserRole.SALESPERSON, "Sales");
        User salesRep12 = createUser("Ava", "Clark", "sales12@crm.com", passwordEncoder.encode("sales12"), UserRole.SALESPERSON, "Sales");

        userRepository.saveAll(Arrays.asList(salesRep1, salesRep2, salesRep3, salesRep4, salesRep5, salesRep6, salesRep7, salesRep8, salesRep9, salesRep10, salesRep11, salesRep12));


        Team teamAlpha = createTeam("Alpha Team", Arrays.asList(salesRep1, salesRep2));
        Team teamBeta = createTeam("Beta Team", Arrays.asList(salesRep3, salesRep4));
        Team teamGamma = createTeam("Gamma Team", Arrays.asList(salesRep5, salesRep6));
        Team teamDelta = createTeam("Delta Team", Arrays.asList(salesRep7, salesRep8));
        Team teamEpsilon = createTeam("Epsilon Team", Arrays.asList(salesRep9, salesRep10));
        Team teamZeta = createTeam("Zeta Team", Arrays.asList(salesRep11, salesRep12));


        salesRep1.setTeam(teamAlpha);
        salesRep2.setTeam(teamAlpha);

        salesRep3.setTeam(teamBeta);
        salesRep4.setTeam(teamBeta);

        salesRep5.setTeam(teamGamma);
        salesRep6.setTeam(teamGamma);

        salesRep7.setTeam(teamDelta);
        salesRep8.setTeam(teamDelta);

        salesRep9.setTeam(teamEpsilon);
        salesRep10.setTeam(teamEpsilon);

        salesRep11.setTeam(teamZeta);
        salesRep12.setTeam(teamZeta);

        teamRepository.saveAll(Arrays.asList(
                teamAlpha, teamBeta, teamGamma, teamDelta, teamEpsilon, teamZeta
        ));

        List<Company> companies = List.of(
                Company.of("TechNova", "Technology"),
                Company.of("HealthPlus", "Healthcare"),
                Company.of("FinCore", "Finance"),
                Company.of("EduSmart", "Education"),
                Company.of("GreenLeaf", "Agriculture"),
                Company.of("AutoMotiveX", "Automotive"),
                Company.of("RetailHub", "Retail"),
                Company.of("ConstructCo", "Construction")
        );
        companyRepository.saveAll(companies);


        List<User> salesRep = userRepository.saveAll(Arrays.asList(salesRep1, salesRep2, salesRep3, salesRep4, salesRep5, salesRep6, salesRep7, salesRep8, salesRep9, salesRep10, salesRep11, salesRep12));
        generateFullSalesData(salesRep);
        generateAccountsAndLeadsForSalesReps(salesRep, companies);


        userRepository.saveAll(users);
    }

    private User createUser(String firstName, String lastName, String email, String password, UserRole role, String position) {
        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(role);
        user.setPosition(position);
        user.setActive(true);
        return user;
    }

    private Team createTeam(String name, List<User> members) {
        Team team = new Team();
        team.setName(name);
        team.setMembers(members);
        return team;
    }

    private void createSale(User salesRep, double amount,
                            LocalDateTime closeDate) {
        Sale sale = new Sale();
        sale.setSalesRep(salesRep);
        sale.setAmount(BigDecimal.valueOf(amount));
        sale.setCloseDate(closeDate);
        saleRepository.save(sale);
    }

    private void generateFullSalesData(List<User> salesReps) {
        int startYear = 2024;
        int endYear = 2025;

        for (User salesRep : salesReps) {
            for (int year = startYear; year <= endYear; year++) {
                int startMonth = (year == startYear) ? 1 : 1;
                int endMonth = (year == endYear) ? 6 : 12;

                for (int month = startMonth; month <= endMonth; month++) {
                    // 2 transakcje na miesiąc
                    createSale(salesRep, randomAmount(), randomDateTimeInMonth(year, month));
                    createSale(salesRep, randomAmount(), randomDateTimeInMonth(year, month));
                }
            }
        }
    }

    // Zwraca losową kwotę między 10 000 a 100 000
    private double randomAmount() {
        return 10000 + Math.random() * 90000;
    }

    // Tworzy losową datę i godzinę w danym miesiącu
    private LocalDateTime randomDateTimeInMonth(int year, int month) {
        int day = Math.min(28, 1 + (int) (Math.random() * 28)); // Unikamy problemów z lutym
        int hour = 9 + (int) (Math.random() * 8);  // między 9 a 16
        int minute = (int) (Math.random() * 60);
        return LocalDateTime.of(year, month, day, hour, minute);
    }
    private void generateAccountsAndLeadsForSalesReps(List<User> salesReps, List<Company> companies) {
        for (User rep : salesReps) {
            for (int i = 1; i <= 5; i++) {
                Account account = new Account();
                account.setUser(rep);
                account.setFirstName("Client" + i);
                account.setLastName("Rep" + rep.getId());
                account.setEmail("client" + i + "_rep" + rep.getId() + "@example.com");
                account.setPhoneNumber("500-600-70" + i);
                account.setAccountStatus(AccountStatus.ACTIVE);
                Company company = companies.get((int) (Math.random() * companies.size()));
                account.setCompany(company);

                accountRepository.save(account);

                for (int j = 1; j <= 3; j++) {
                    Lead lead = new Lead();
                    lead.setAccount(account);
                    String companyName = account.getCompany() != null ? account.getCompany().getName() : "Unknown Company";
                    lead.setDescription("Potential deal " + j + " for " + companyName);
                    lead.setEstimatedValue(BigDecimal.valueOf(5000 + Math.random() * 15000));
                    // Losowy status
                    LeadStatus[] statuses = LeadStatus.values();
                    lead.setStatus(statuses[(int) (Math.random() * statuses.length)]);

                    leadRepository.save(lead);
                }
            }
        }
    }



}

