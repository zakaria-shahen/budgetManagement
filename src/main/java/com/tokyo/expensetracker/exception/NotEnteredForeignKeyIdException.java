package com.tokyo.expensetracker.exception;

public class NotEnteredForeignKeyIdException extends RuntimeException{
    public NotEnteredForeignKeyIdException(String massage){
        super(massage);
    }
}
