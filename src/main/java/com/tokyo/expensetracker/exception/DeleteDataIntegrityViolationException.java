package com.tokyo.expensetracker.exception;

public class DeleteDataIntegrityViolationException extends RuntimeException {
     public DeleteDataIntegrityViolationException(String message){
         super(message);
     }
}
