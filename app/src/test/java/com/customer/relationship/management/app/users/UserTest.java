package com.customer.relationship.management.app.users;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    private final User testUser = TestUserUtils.getTestUser();

    @Test
    void shouldBeActiveByDefault() {
        assertTrue(testUser.isActive());
    }

    @Test
    void shouldHaveCreatedDate() {
        assertNotNull(testUser.getCreatedAt());
    }

    @Test
    void shouldHaveUpdatedDate() {
        assertNotNull(testUser.getUpdatedAt());
    }
}