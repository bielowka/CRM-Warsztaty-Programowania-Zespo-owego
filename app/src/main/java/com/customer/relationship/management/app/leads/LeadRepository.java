package com.customer.relationship.management.app.leads;

import com.customer.relationship.management.app.accounts.Account;
import com.customer.relationship.management.app.users.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LeadRepository extends JpaRepository<Lead, Long> {

    Page<Lead> findByAccountUser(User user, Pageable pageable);
    
    List<Lead> findByAccount(Account account);
}