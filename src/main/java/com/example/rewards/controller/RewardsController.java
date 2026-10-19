package com.example.rewards.controller;

import com.example.rewards.api.CustomerRewards;
import com.example.rewards.api.RewardsReport;
import com.example.rewards.exception.CustomerNotFoundException;
import com.example.rewards.service.RewardsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.time.YearMonth;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.format.annotation.DateTimeFormat;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(value = "/api/v1/rewards", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Customer Rewards", description = "Rewards for the requested three calendar months")
@Slf4j
public class RewardsController {
    private final RewardsService service;

    public RewardsController(RewardsService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "Get monthly rewards and totals for every customer")
    public RewardsReport report(@Parameter(description = "First month, yyyy-MM")
                                    @RequestParam
                                    @DateTimeFormat(pattern = "yyyy-MM") YearMonth startMonth) {
        log.info("GET /api/v1/rewards startMonth={}", startMonth);
        return service.report(startMonth);
    }

    @GetMapping("/{customerId}")
    @Operation(summary = "Get monthly rewards and total for one customer",
            description = "Returns 404 if the customer has no transactions in the period")
    public CustomerRewards customer(@PathVariable String customerId,
                                    @Parameter(description = "First month, yyyy-MM")
                                    @RequestParam @DateTimeFormat(pattern = "yyyy-MM") YearMonth startMonth) {
        log.info("GET /api/v1/rewards/{} startMonth={}", customerId, startMonth);
        return service.customerReport(startMonth, customerId)
                .orElseThrow(() -> new CustomerNotFoundException(customerId));
    }
}
