package com.myCompany.budgetManagement.exception;

public class NotEnteredAllRequiredFieldException extends RuntimeException {
    public NotEnteredAllRequiredFieldException(String message) {
        super(message);
    }
}
