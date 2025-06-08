package com.customer.relationship.management.app.sales;

import com.customer.relationship.management.app.users.User;
import com.customer.relationship.management.app.users.UserRepository;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SalesRankingService {

    private final SaleRepository saleRepository;
    private final UserRepository userRepository;

    public SalesRankingService(
            SaleRepository saleRepository,
            UserRepository userRepository
    ) {
        this.saleRepository = saleRepository;
        this.userRepository = userRepository;
    }

    public List<SalesRankingDTO> getSalesRanking(int year, int month) {
        List<Object[]> results = saleRepository.findSalesPerformance(year, month);
        Map<Long, SalesAggregate> aggregates = new HashMap<>();

        for (Object[] result : results) {
            Long userId = (Long) result[0];
            BigDecimal total = (BigDecimal) result[1];
            Long count = (Long) result[2];
            aggregates.put(userId, new SalesAggregate(total, count));
        }

        return aggregates.entrySet().stream()
                .map(entry -> {
                    Long userId = entry.getKey();
                    SalesAggregate agg = entry.getValue();
                    User user = userRepository.findById(userId).orElseThrow();
                    return new SalesRankingDTO(
                            userId,
                            user.getFirstName(),
                            user.getLastName(),
                            agg.getTotalAmount(),
                            agg.getDealsCount()
                    );
                })
                .sorted(Comparator
                        .comparing(SalesRankingDTO::getTotalAmount).reversed()
                        .thenComparing(SalesRankingDTO::getDealsCount).reversed()
                )
                .collect(Collectors.toList());
    }

    private static class SalesAggregate {
        private final BigDecimal totalAmount;
        private final Long dealsCount;

        public SalesAggregate(BigDecimal totalAmount, Long dealsCount) {
            this.totalAmount = totalAmount;
            this.dealsCount = dealsCount;
        }

        public BigDecimal getTotalAmount() {
            return totalAmount;
        }

        public Long getDealsCount() {
            return dealsCount;
        }
    }
}