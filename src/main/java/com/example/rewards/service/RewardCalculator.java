package com.example.rewards.service;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

@Component
public class RewardCalculator {
    private static final BigDecimal FIFTY = new BigDecimal("50");
    private static final BigDecimal HUNDRED = new BigDecimal("100");

    public long calculate(BigDecimal amount) {

        if (amount == null || amount.signum() < 0) {
            throw new IllegalArgumentException("Transaction amount must be non-negative");
        }
        long onePointDollars = amount.min(HUNDRED).subtract(FIFTY).max(BigDecimal.ZERO).toBigInteger().longValueExact();
        long twoPointDollars = amount.subtract(HUNDRED).max(BigDecimal.ZERO).toBigInteger().longValueExact();
        return Math.addExact(onePointDollars, Math.multiplyExact(twoPointDollars, 2));
    }
}
