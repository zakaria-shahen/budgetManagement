package com.myCompany.budgetManagement.exception;

public class NotEnteredForeignKeyIdException extends RuntimeException{
    public NotEnteredForeignKeyIdException(String massage){
        super(massage);
    }
}
