package com.example.rewards.service;

import com.example.rewards.api.CustomerRewards;
import com.example.rewards.api.MonthlyRewards;
import com.example.rewards.api.RewardsReport;
import com.example.rewards.domain.Transaction;
import com.example.rewards.repository.TransactionRepository;

import java.time.YearMonth;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
        Map<String, Map<YearMonth, Long>> grouped = repository.findByPurchaseDateGreaterThanEqualAndPurchaseDateLessThan(startDate, endDateExclusive).stream()
            .filter(tx -> !YearMonth.from(tx.getPurchaseDate())
                .isBefore(startMonth) && !YearMonth.from(tx.getPurchaseDate())
                .isAfter(endMonth))
                .collect(Collectors.groupingBy(Transaction::getCustomerId, TreeMap::new,
                    Collectors.groupingBy(tx -> YearMonth.from(tx.getPurchaseDate()), TreeMap::new,
                    Collectors.reducing(0L, tx -> calculator.calculate(tx.getAmount()), Math::addExact))));

        List<CustomerRewards> customers = grouped.entrySet().stream().map(entry -> {
            List<MonthlyRewards> months =
                    IntStream.range(0, 3).mapToObj(offset -> {
                        YearMonth month = startMonth.plusMonths(offset);
                return new MonthlyRewards(month, entry.getValue().getOrDefault(month, 0L));
            }).collect(Collectors.toList());
            long total = months.stream().mapToLong(MonthlyRewards::getPoints).reduce(0L, Math::addExact);
            return new CustomerRewards(entry.getKey(), months, total);
        }).collect(Collectors.toList());
        return new RewardsReport(startMonth, endMonth, customers);
    }

    public Optional<CustomerRewards> customerReport(YearMonth startMonth, String customerId) {
        log.info("Generating customer report for customerId={} starting {}", customerId, startMonth);
        return report(startMonth).getCustomers().stream()
                .filter(customer -> customer.getCustomerId().equals(customerId)).findFirst();
    }
}
