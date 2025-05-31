package com.customer.relationship.management.app.users;

public final class UsersFixture {
    public static User getTestUser(String email) {
        User newUser = new User();
        newUser.setFirstName("John");
        newUser.setLastName("Doe");
        newUser.setEmail(email);
        newUser.setPassword("a");
        newUser.setRole(UserRole.SALESPERSON);
        return newUser;
    }
}
