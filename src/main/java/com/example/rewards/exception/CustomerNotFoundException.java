package com.example.rewards.exception;

public class CustomerNotFoundException extends RuntimeException {
    public CustomerNotFoundException(String id) {
        super("No transactions found for customer " + id + " in the requested three-month period");
    }
}
