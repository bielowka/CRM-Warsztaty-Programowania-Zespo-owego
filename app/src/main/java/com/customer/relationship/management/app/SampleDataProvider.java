package com.customer.relationship.management.app;

import com.customer.relationship.management.app.users.User;
import com.customer.relationship.management.app.users.UserRole;
import com.customer.relationship.management.app.users.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class SampleDataProvider {

    private final UserRepository userRepository;

    public SampleDataProvider(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostConstruct
    public void init() {
        if (userRepository.findAll().stream().noneMatch(user -> user.getEmail().equals("admin@admin"))) {
            User user = new User();
            user.setFirstName("John");
            user.setLastName("Doe");
            user.setEmail("admin@admin");
            user.setPassword("admin");
            user.setRole(UserRole.ADMIN);
            userRepository.save(user);
        }

        userRepository.findAll().forEach(System.out::println);
    }
}