package com.example.rewards.api;

import java.time.YearMonth;
import lombok.Value;

@Value
public class MonthlyRewards {
    YearMonth month;
    long points;
}
