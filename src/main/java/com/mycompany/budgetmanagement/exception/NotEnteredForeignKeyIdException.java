package com.mycompany.budgetmanagement.exception;

public class NotEnteredForeignKeyIdException extends RuntimeException{
    public NotEnteredForeignKeyIdException(String massage){
        super(massage);
    }
}
