package com.customer.relationship.management.app;

import com.customer.relationship.management.app.users.User;
import com.customer.relationship.management.app.users.UserRole;

public final class TestEntitiesUtils {
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
