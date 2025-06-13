package com.customer.relationship.management.app.accounts;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NoteRepository extends JpaRepository<Note, Long> {
    List<Note> findByAccountId(Long accountId, Sort sort);
} 