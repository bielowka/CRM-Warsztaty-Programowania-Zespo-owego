package com.customer.relationship.management.app.users;

import com.customer.relationship.management.app.TestEntitiesUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    private final User testUser = TestEntitiesUtils.getTestUser("a");

    @Test
    void shouldBeActiveByDefault() {
        assertTrue(testUser.isActive());
    }
}