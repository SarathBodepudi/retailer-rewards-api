package com.example.rewards.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "purchase_transactions")
public class Transaction {
    @Id
    @Column(name = "transaction_id", nullable = false)
    private String transactionId;

    @Column(name = "customer_id", nullable = false)
    private String customerId;

    @Column(name = "purchase_date", nullable = false)
    private LocalDate purchaseDate;

    @Column(name = "amount", nullable = false, precision = 12, scale = 2)
    private BigDecimal amount;

    protected Transaction() { }

    public Transaction(String transactionId,
                       String customerId,
                       LocalDate purchaseDate,
                       BigDecimal amount) {
        this.transactionId = Objects.requireNonNull(transactionId);
        this.customerId = Objects.requireNonNull(customerId);
        this.purchaseDate = Objects.requireNonNull(purchaseDate);
        this.amount = Objects.requireNonNull(amount);
        if (amount.signum() < 0) throw new IllegalArgumentException("Amount must be non-negative");
    }

    public String getTransactionId() { return transactionId; }
    public String getCustomerId() { return customerId; }
    public LocalDate getPurchaseDate() { return purchaseDate; }
    public BigDecimal getAmount() { return amount; }
}
