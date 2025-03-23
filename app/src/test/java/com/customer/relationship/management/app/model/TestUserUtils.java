package com.customer.relationship.management.app.model;

public final class TestUserUtils {
    public static User getTestUser() {
        User newUser = new User();
        newUser.setFirstName("John");
        newUser.setLastName("Doe");
        newUser.setEmail("a@a");
        newUser.setPassword("a");
        newUser.setRole(UserRole.SALESPERSON);
        return newUser;
    }
}
