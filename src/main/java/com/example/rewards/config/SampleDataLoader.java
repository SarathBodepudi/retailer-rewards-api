package com.example.rewards.config;

import com.example.rewards.domain.Transaction;
import com.example.rewards.repository.TransactionRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SampleDataLoader {
    @Bean
    CommandLineRunner loadDemoTransactions(TransactionRepository repository) {
        return args -> {
            if (repository.count() != 0) return;
            repository.saveAll(Arrays.asList(
                    tx("T001", "C001", "2026-01-01", "120.00"),
                    tx("T002", "C001", "2026-01-15", "75.00"),
                    tx("T003", "C001", "2026-02-04", "200.00"),
                    tx("T004", "C001", "2026-03-31", "50.00"),
                    tx("T005", "C002", "2026-01-03", "49.99"),
                    tx("T006", "C002", "2026-02-11", "100.00"),
                    tx("T007", "C002", "2026-02-17", "101.00"),
                    tx("T008", "C002", "2026-03-09", "150.00"),
                    tx("T009", "C003", "2026-01-20", "250.00"),
                    tx("T010", "C003", "2026-03-20", "120.50"),
                    tx("T011", "C003", "2026-03-29", "50.99"),
                    tx("T012", "C004", "2026-02-22", "75.00"),
                    tx("T013", "C001", "2025-12-31", "500.00"),
                    tx("T014", "C002", "2026-04-01", "500.00")
            ));
        };
    }

    private static Transaction tx(String id, String customerId, String date, String amount) {
        return new Transaction(id, customerId, LocalDate.parse(date), new BigDecimal(amount));
    }
}
