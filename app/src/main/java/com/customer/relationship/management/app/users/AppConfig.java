package com.customer.relationship.management.app.users;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    UserService userService(UserRepository userRepository) {
        return new UserService(userRepository);
    }

    public UserService userService() {
        return new UserService(new InMemoryUserRepository());
    }
}
