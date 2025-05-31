package com.customer.relationship.management.app.users;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    private final User testUser = UsersFixture.getTestUser("a");

    @Test
    void shouldBeActiveByDefault() {
        assertTrue(testUser.isActive());
    }
}