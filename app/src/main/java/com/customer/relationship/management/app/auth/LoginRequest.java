package com.customer.relationship.management.app.auth;

import lombok.Data;

@Data
public class LoginRequest {
    private String email;
    private String password;
}