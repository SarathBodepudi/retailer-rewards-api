package com.example.rewards.repository;

import com.example.rewards.domain.Transaction;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class TransactionRepositoryJpaTest {
    @Autowired TransactionRepository repository;

    @Test
    void filtersByInclusiveStartExclusiveEnd() {
        repository.save(new Transaction("TEST1", "C001", LocalDate.of(2026, 1, 1), new BigDecimal("120")));
        repository.save(new Transaction("TEST2", "C001", LocalDate.of(2026, 4, 1), new BigDecimal("500")));
        assertEquals(1, repository.findByPurchaseDateGreaterThanEqualAndPurchaseDateLessThan(
            LocalDate.of(2026, 1, 1), LocalDate.of(2026, 4, 1)).size());
    }
}
