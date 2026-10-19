package com.example.rewards.repository;

import com.example.rewards.domain.Transaction;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, String> {
    List<Transaction> findByPurchaseDateGreaterThanEqualAndPurchaseDateLessThan(LocalDate startInclusive, LocalDate endExclusive);
}
