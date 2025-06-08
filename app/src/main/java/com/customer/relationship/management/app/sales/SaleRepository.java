package com.customer.relationship.management.app.sales;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SaleRepository extends JpaRepository<Sale, Long> {

    @Query("SELECT s.salesRep.id, SUM(s.amount), COUNT(s) " +
            "FROM Sale s " +
            "WHERE YEAR(s.closeDate) = :year AND MONTH(s.closeDate) = :month " +
            "GROUP BY s.salesRep.id")
    List<Object[]> findSalesPerformance(@Param("year") int year, @Param("month") int month);


    @Query("SELECT t.id, t.name, SUM(s.amount), COUNT(s), COUNT(DISTINCT sr) " +
            "FROM Sale s " +
            "JOIN s.salesRep sr " +
            "JOIN sr.team t " +
            "WHERE YEAR(s.closeDate) = :year " +
            "AND MONTH(s.closeDate) = :month " +
            "GROUP BY t.id, t.name")
    List<Object[]> findTeamSalesPerformance(
            @Param("year") int year,
            @Param("month") int month
    );
}