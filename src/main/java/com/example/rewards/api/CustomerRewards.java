package com.example.rewards.api;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lombok.Value;

@Value
public class CustomerRewards {
    String customerId;
    List<MonthlyRewards> monthlyRewards;
    long totalPoints;

    public CustomerRewards(String customerId, List<MonthlyRewards> monthlyRewards, long totalPoints) {
        this.customerId = customerId;
        this.monthlyRewards = Collections.unmodifiableList(new ArrayList<>(monthlyRewards));
        this.totalPoints = totalPoints;
    }
}
