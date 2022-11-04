package com.tokyo.expensetracker.exception;

public class NotAllowedDeleteLastOwnerException extends RuntimeException {
    public NotAllowedDeleteLastOwnerException(String s) {
        super(s);

    }
}
