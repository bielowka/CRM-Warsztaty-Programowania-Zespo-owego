package com.customer.relationship.management.app.accounts;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CompanyRepository extends JpaRepository<Company, Long> {
    Optional<Company> findByNameAndIndustry(String name, String industry);
}
