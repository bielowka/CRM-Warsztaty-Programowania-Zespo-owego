package com.customer.relationship.management.app.auth;

import com.customer.relationship.management.app.users.UserRole;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthResponse {
    private String token;
    private Long userId;
    private String email;
    private String firstName;
    private String lastName;
    private UserRole role;
    private boolean authenticated;
} 