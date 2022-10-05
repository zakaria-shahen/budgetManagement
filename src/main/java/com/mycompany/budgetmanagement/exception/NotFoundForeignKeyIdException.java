package com.mycompany.budgetmanagement.exception;

public class NotFoundForeignKeyIdException extends RuntimeException {
    public NotFoundForeignKeyIdException(String massage) {
        super(massage);
    }
}
