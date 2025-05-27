package com.customer.relationship.management.app.config.database;

import com.customer.relationship.management.app.users.User;
import com.customer.relationship.management.app.users.UserRepository;
import com.customer.relationship.management.app.users.UserRole;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DatabaseInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DatabaseInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (userRepository.count() > 0) {
            return;
        }

        List<User> users = List.of(
                createUser("Jan", "Kowalski", "jan@example.com", "password123", UserRole.ADMIN, "CEO"),
                createUser("Anna", "Nowak", "anna@example.com", "password123", UserRole.SALESPERSON, "Sales"),
                createUser("Piotr", "Zieliński", "piotr@example.com", "password123", UserRole.SALESPERSON, "Support"),
                createUser("Kasia", "Wiśniewska", "kasia@example.com", "password123", UserRole.MANAGER, "Marketing"),
                createUser("Tomek", "Lis", "tomek@example.com", "password123", UserRole.SALESPERSON, "IT"),
                createUser("Ewa", "Malinowska", "ewa@example.com", "password123", UserRole.SALESPERSON, "HR"),
                createUser("Marek", "Mazur", "marek@example.com", "password123", UserRole.SALESPERSON, "Logistics"),
                createUser("Natalia", "Kaczmarek", "natalia@example.com", "password123", UserRole.SALESPERSON, "Legal"),
                createUser("Paweł", "Wójcik", "pawel@example.com", "password123", UserRole.SALESPERSON, "Finance"),
                createUser("Zofia", "Dąbrowska", "zofia@example.com", "password123", UserRole.SALESPERSON, "Design")
        );

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
}

