package com.tokyo.expensetracker.exception;

public class NotFoundForeignKeyIdException extends RuntimeException {
    public NotFoundForeignKeyIdException(String massage) {
        super(massage);
    }
}
