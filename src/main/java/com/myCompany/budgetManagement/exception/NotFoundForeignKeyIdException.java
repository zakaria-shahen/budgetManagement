package com.myCompany.budgetManagement.exception;

public class NotFoundForeignKeyIdException extends RuntimeException {
    public NotFoundForeignKeyIdException(String massage) {
        super(massage);
    }
}
