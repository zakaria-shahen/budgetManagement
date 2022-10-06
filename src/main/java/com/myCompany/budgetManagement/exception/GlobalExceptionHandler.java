package com.myCompany.budgetManagement.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.CannotCreateTransactionException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.net.ConnectException;

@RestController
@ControllerAdvice(basePackages = "com.myCompany.budgetManagement")
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorMessage> NotFoundHandler(HttpServletRequest request, Exception e) {
        var errorMassage = new ErrorMessage(
                HttpStatus.NOT_FOUND,
                e.getMessage(),
                request.getRequestURI());
        return new ResponseEntity<>(errorMassage, errorMassage.status());
    }

    @ExceptionHandler(NotFoundForeignKeyIdException.class)
    public ResponseEntity<ErrorMessage> NotFoundForeignKeyIdHandle(HttpServletRequest request, Exception e) {
        var errorMassage = new ErrorMessage(
                HttpStatus.CONFLICT,
                e.getMessage(),
                request.getRequestURI());
        return new ResponseEntity<>(errorMassage, errorMassage.status());
    }

    @ExceptionHandler(NotEnteredForeignKeyIdException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorMessage> NotEnteredForeignKeyIdHandle(HttpServletRequest request, Exception e) {
        var errorMassage = new ErrorMessage(
                HttpStatus.BAD_REQUEST,
                e.getMessage(),
                request.getRequestURI());
        return new ResponseEntity<>(errorMassage, errorMassage.status());
    }


    @ExceptionHandler({ConnectException.class, CannotCreateTransactionException.class})
    public ResponseEntity<ErrorMessage> DatabaseConnectHandler(HttpServletRequest request) {
        var errorMassage = new ErrorMessage(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "DB Connection refused",
                request.getRequestURI());
        return new ResponseEntity<>(errorMassage, errorMassage.status());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ArgumentNotValidResponseBuilder>
        MethodArgumentNotValidHandler(HttpServletRequest request, MethodArgumentNotValidException e){
        var repo = new ArgumentNotValidResponseBuilder(request.getRequestURI(), HttpStatus.BAD_REQUEST, e);
        return repo.responseEntityBuild();
    }

}
