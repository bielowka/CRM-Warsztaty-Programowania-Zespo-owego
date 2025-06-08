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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.List;

@Component
public class DatabaseInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TeamRepository teamRepository;
    private final AccountRepository accountRepository;
    private final SaleRepository saleRepository;

    public DatabaseInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder, TeamRepository teamRepository,
                               AccountRepository accountRepository, SaleRepository saleRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.teamRepository = teamRepository;
        this.accountRepository = accountRepository;
        this.saleRepository = saleRepository;

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

        User salesRep1 = createUser("Mike", "Johnson", "sales1@crm.com", passwordEncoder.encode("sales1"), UserRole.SALESPERSON, "Sales");
        User salesRep2 = createUser("Sarah", "Williams", "sales2@crm.com", passwordEncoder.encode("sales2"), UserRole.SALESPERSON, "Sales");
        User salesRep3 = createUser("Tom", "Brown", "sales3@crm.com", passwordEncoder.encode("sales3"), UserRole.SALESPERSON, "Sales");
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

        Account account1 = createAccount(salesRep1.getFirstName(), salesRep1.getLastName(), salesRep1.getEmail(), "+1 555-9012", AccountStatus.ACTIVE, salesRep1);
        Account account2 = createAccount(salesRep2.getFirstName(), salesRep2.getLastName(), salesRep2.getEmail(), "+1 555-9012", AccountStatus.ACTIVE, salesRep2);
        Account account3 = createAccount(salesRep3.getFirstName(), salesRep3.getLastName(), salesRep3.getEmail(), "+1 555-9012", AccountStatus.ACTIVE, salesRep3);
        Account account4 = createAccount(salesRep4.getFirstName(), salesRep4.getLastName(), salesRep4.getEmail(), "+1 555-9012", AccountStatus.ACTIVE, salesRep4);
        Account account5 = createAccount(salesRep5.getFirstName(), salesRep5.getLastName(), salesRep5.getEmail(), "+1 555-9012", AccountStatus.ACTIVE, salesRep5);
        Account account6 = createAccount(salesRep6.getFirstName(), salesRep6.getLastName(), salesRep6.getEmail(), "+1 555-9012", AccountStatus.ACTIVE, salesRep6);
        Account account7 = createAccount(salesRep7.getFirstName(), salesRep7.getLastName(), salesRep7.getEmail(), "+1 555-9012", AccountStatus.ACTIVE, salesRep7);
        Account account8 = createAccount(salesRep8.getFirstName(), salesRep8.getLastName(), salesRep8.getEmail(), "+1 555-9012", AccountStatus.ACTIVE, salesRep8);
        Account account9 = createAccount(salesRep9.getFirstName(), salesRep9.getLastName(), salesRep9.getEmail(), "+1 555-9012", AccountStatus.ACTIVE, salesRep9);
        Account account10 = createAccount(salesRep10.getFirstName(), salesRep10.getLastName(), salesRep10.getEmail(), "+1 555-9012", AccountStatus.ACTIVE, salesRep10);
        Account account11 = createAccount(salesRep11.getFirstName(), salesRep11.getLastName(), salesRep11.getEmail(), "+1 555-9012", AccountStatus.ACTIVE, salesRep11);
        Account account12 = createAccount(salesRep12.getFirstName(), salesRep12.getLastName(), salesRep12.getEmail(), "+1 555-9012", AccountStatus.ACTIVE, salesRep12);


        Team teamAlpha = createTeam("Alpha Team", Arrays.asList(account1, account2));
        Team teamBeta = createTeam("Beta Team", Arrays.asList(account3, account4));
        Team teamGamma = createTeam("Gamma Team", Arrays.asList(account5, account6));
        Team teamDelta = createTeam("Delta Team", Arrays.asList(account7, account8));
        Team teamEpsilon = createTeam("Epsilon Team", Arrays.asList(account9, account10));
        Team teamZeta = createTeam("Zeta Team", Arrays.asList(account11, account12));


        account1.setTeam(teamAlpha);
        account2.setTeam(teamAlpha);

        account3.setTeam(teamBeta);
        account4.setTeam(teamBeta);

        account5.setTeam(teamGamma);
        account6.setTeam(teamGamma);

        account7.setTeam(teamDelta);
        account8.setTeam(teamDelta);

        account9.setTeam(teamEpsilon);
        account10.setTeam(teamEpsilon);

        account11.setTeam(teamZeta);
        account12.setTeam(teamZeta);

        teamRepository.saveAll(Arrays.asList(
                teamAlpha, teamBeta, teamGamma, teamDelta, teamEpsilon, teamZeta
        ));

        accountRepository.saveAll(Arrays.asList(
                account1, account2, account3, account4,
                account5, account6, account7, account8,
                account9, account10, account11, account12
        ));

        List<Account> salesRep = accountRepository.saveAll(Arrays.asList(account1, account2, account3, account4, account5, account6, account7, account8, account9, account10, account11, account12));
        generateFullSalesData(salesRep);


        // Deale dla Mike'a Johnsona (salesRep1)
        createSale(account1, 48000.0, LocalDateTime.of(2025, Month.JANUARY, 15, 10, 0));
        createSale(account1, 32000.0, LocalDateTime.of(2025, Month.JANUARY, 22, 14, 30));
        createSale(account1, 37000.0, LocalDateTime.of(2025, Month.FEBRUARY, 5, 11, 15));

        // Deale dla Sarah Williams (salesRep2)
        createSale(account2, 82000.0, LocalDateTime.of(2025, Month.JANUARY, 10, 9, 0));
        createSale(account2, 15000.0, LocalDateTime.of(2025, Month.FEBRUARY, 12, 16, 45));

        // Deale dla Toma Browna (salesRep3)
        createSale(account3, 46000.0, LocalDateTime.of(2025, Month.JANUARY, 28, 13, 20));
        createSale(account3, 25000.0, LocalDateTime.of(2025, Month.FEBRUARY, 15, 10, 30));

        // Deale dla Emily Davis (salesRep4)
        createSale(account4, 31000.0, LocalDateTime.of(2025, Month.FEBRUARY, 8, 11, 0));
        createSale(account4, 42000.0, LocalDateTime.of(2025, Month.FEBRUARY, 20, 14, 0));

        userRepository.saveAll(users);
    }

    private Account createAccount(String firstName, String lastName, String email, String phone,
                                  AccountStatus status, User user) {
        Account account = new Account();
        account.setFirstName(firstName);
        account.setLastName(lastName);
        account.setEmail(email);
        account.setPhoneNumber(phone);
        account.setAccountStatus(status);
        account.setUser(user);
        return account;
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

    private Team createTeam(String name, List<Account> members) {
        Team team = new Team();
        team.setName(name);
        team.setMembers(members);
        return team;
    }

    private void createSale(Account salesRep, double amount,
                            LocalDateTime closeDate) {
        Sale sale = new Sale();
        sale.setSalesRep(salesRep);
        sale.setAmount(BigDecimal.valueOf(amount));
        sale.setCloseDate(closeDate);
        saleRepository.save(sale);
    }

    private void generateFullSalesData(List<Account> salesReps) {
        int startYear = 2024;
        int endYear = 2025;

        for (Account salesRep : salesReps) {
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

}

