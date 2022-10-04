package com.myCompany.budgetManagement.exception;

public class NotFoundException extends  RuntimeException {
    public NotFoundException(String massage) {
        super(massage);
    }
}
