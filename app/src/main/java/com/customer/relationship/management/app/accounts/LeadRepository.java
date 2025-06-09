package com.customer.relationship.management.app.accounts;

import com.customer.relationship.management.app.users.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LeadRepository extends JpaRepository<Lead, Long> {
    List<Lead> findByAccountUser(User user);
}