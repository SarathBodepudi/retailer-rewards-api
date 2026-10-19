package com.example.rewards.api;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lombok.Value;

@Value
public class RewardsReport {
    YearMonth startMonth;
    YearMonth endMonth;
    List<CustomerRewards> customers;

    public RewardsReport(YearMonth startMonth, YearMonth endMonth, List<CustomerRewards> customers) {
        this.startMonth = startMonth;
        this.endMonth = endMonth;
        this.customers = Collections.unmodifiableList(new ArrayList<>(customers));
    }
}
