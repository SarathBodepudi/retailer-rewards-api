package com.example.rewards.service;

import com.example.rewards.api.CustomerRewards;
import com.example.rewards.api.MonthlyRewards;
import com.example.rewards.api.RewardsReport;
import com.example.rewards.domain.Transaction;
import com.example.rewards.repository.TransactionRepository;

import java.time.YearMonth;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class RewardsService {
    private final TransactionRepository repository;
    private final RewardCalculator calculator;


    public RewardsReport report(YearMonth startMonth) {
        log.info("Generating rewards report starting {}", startMonth);
        YearMonth endMonth = startMonth.plusMonths(2);
        LocalDate startDate = startMonth.atDay(1);
        LocalDate endDateExclusive = startMonth.plusMonths(3).atDay(1);

        List<Transaction> transactions = repository
                .findByPurchaseDateGreaterThanEqualAndPurchaseDateLessThan(startDate, endDateExclusive);

        Map<String, Map<YearMonth, Long>> grouped = groupPointsByCustomerAndMonth(transactions, startMonth, endMonth);

        List<CustomerRewards> customers = buildCustomerRewards(grouped, startMonth);

        return new RewardsReport(startMonth, endMonth, customers);
    }

    private Map<String, Map<YearMonth, Long>> groupPointsByCustomerAndMonth(List<Transaction> transactions, YearMonth startMonth, YearMonth endMonth) {
        Map<String, Map<YearMonth, Long>> grouped = new TreeMap<>();
        for (Transaction tx : transactions) {
            YearMonth month = YearMonth.from(tx.getPurchaseDate());
            if (month.isBefore(startMonth) || month.isAfter(endMonth)) continue;
            long points = calculator.calculate(tx.getAmount());
            grouped.computeIfAbsent(tx.getCustomerId(), k -> new TreeMap<>()).merge(month, points, Math::addExact);
        }
        return grouped;
    }

    private List<CustomerRewards> buildCustomerRewards(Map<String, Map<YearMonth, Long>> grouped, YearMonth startMonth) {
        List<CustomerRewards> customers = new ArrayList<>();
        for (Map.Entry<String, Map<YearMonth, Long>> entry : grouped.entrySet()) {
            List<MonthlyRewards> months = new ArrayList<>(3);
            long total = 0L;
            for (int offset = 0; offset < 3; offset++) {
                YearMonth m = startMonth.plusMonths(offset);
                long pts = entry.getValue().getOrDefault(m, 0L);
                months.add(new MonthlyRewards(m, pts));
                total = Math.addExact(total, pts);
            }
            customers.add(new CustomerRewards(entry.getKey(), months, total));
        }
        return customers;
    }

    public Optional<CustomerRewards> customerReport(YearMonth startMonth, String customerId) {
        log.info("Generating customer report for customerId={} starting {}", customerId, startMonth);
        return report(startMonth).getCustomers().stream()
                .filter(customer -> customer.getCustomerId().equals(customerId)).findFirst();
    }
}
