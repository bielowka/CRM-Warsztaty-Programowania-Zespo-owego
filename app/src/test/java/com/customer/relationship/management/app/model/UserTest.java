package com.customer.relationship.management.app.model;

import com.customer.relationship.management.app.users.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void shouldBeActiveByDefault() {
        User newUser = new User();
        assertTrue(newUser.isActive());
    }

    @Test
    void shouldHaveCreatedDate() {
        User newUser = new User();
        assertNotNull(newUser.getCreatedAt());
    }

    @Test
    void shouldHaveUpdatedDate() {
        User newUser = new User();
        assertNotNull(newUser.getUpdatedAt());
    }
}