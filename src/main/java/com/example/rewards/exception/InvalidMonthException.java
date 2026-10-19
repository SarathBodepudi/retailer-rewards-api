package com.example.rewards.exception;

public class InvalidMonthException extends RuntimeException {
    public InvalidMonthException() {
        super("startMonth must use yyyy-MM (example: 2026-01)");
    }
}
