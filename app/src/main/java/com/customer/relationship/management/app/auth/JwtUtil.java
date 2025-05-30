package com.customer.relationship.management.app.auth;

import com.customer.relationship.management.app.users.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {
    private static final long EXPIRATION_TIME = 864_000_000; // 10 days
    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    public String generateToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", user.getRole().name());
        claims.put("userId", user.getId());
        claims.put("email", user.getEmail());

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(user.getEmail())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(key)
                .compact();
    }

    public Claims validateToken(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Invalid token format");
        }

        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(authHeader.replace("Bearer ", ""))
                .getBody();
    }
} 