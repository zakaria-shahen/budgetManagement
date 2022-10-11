package com.tokyo.expensetracker.exception;

public class NotFoundException extends  RuntimeException {
    public NotFoundException(String massage) {
        super(massage);
    }
}
