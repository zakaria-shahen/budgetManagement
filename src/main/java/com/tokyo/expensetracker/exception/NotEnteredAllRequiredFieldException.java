package com.tokyo.expensetracker.exception;

public class NotEnteredAllRequiredFieldException extends RuntimeException {
    public NotEnteredAllRequiredFieldException(String message) {
        super(message);
    }
}
