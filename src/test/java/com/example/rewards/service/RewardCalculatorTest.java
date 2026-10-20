package com.example.rewards.service;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

class RewardCalculatorTest {
    private final RewardCalculator calculator = new RewardCalculator();

    @Test
    void demonstratesExampleAndTierBoundaries() {
        assertEquals(0, points("0"));
        assertEquals(0, points("49.99"));
        assertEquals(0, points("50.00"));
        assertEquals(25, points("75.00"));
        assertEquals(50, points("100.00"));
        assertEquals(52, points("101.00"));
        assertEquals(90, points("120.00"));
        assertEquals(90, points("120.50"));
        assertEquals(250, points("200.00"));
    }

    @Test
    void refusesNegativeAmounts() {
        assertThrows(IllegalArgumentException.class, () -> points("-1"));
    }

    private long points(String amount) {
        return calculator.calculate(new BigDecimal(amount));
    }
}
