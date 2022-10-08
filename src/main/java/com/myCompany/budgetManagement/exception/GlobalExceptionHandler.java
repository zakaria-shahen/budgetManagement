package com.myCompany.budgetManagement.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.CannotCreateTransactionException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.net.ConnectException;

@RestController
@ControllerAdvice(basePackages = "com.myCompany.budgetManagement")
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorMessage> NotFoundHandler(HttpServletRequest request, Exception e) {
        var errorMassage = new ErrorMessage(
                HttpStatus.NOT_FOUND,
                e.getMessage(),
                request.getRequestURI());
        return new ResponseEntity<>(errorMassage, errorMassage.status());
    }

    @ExceptionHandler({NotFoundForeignKeyIdException.class, DeleteDataIntegrityViolationException.class})
    public ResponseEntity<ErrorMessage> conflictHandler(HttpServletRequest request, Exception e) {
        var errorMassage = new ErrorMessage(
                HttpStatus.CONFLICT,
                e.getMessage(),
                request.getRequestURI());
        return new ResponseEntity<>(errorMassage, errorMassage.status());
    }

    @ExceptionHandler({NotEnteredForeignKeyIdException.class, NotEnteredAllRequiredFieldException.class})
    public ResponseEntity<ErrorMessage> badRequestHandler(HttpServletRequest request, Exception e) {
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
    public ResponseEntity<ArgumentNotValidResponseBuilder>
        MethodArgumentNotValidHandler(HttpServletRequest request, MethodArgumentNotValidException e){
        var repo = new ArgumentNotValidResponseBuilder(request.getRequestURI(), HttpStatus.BAD_REQUEST, e);
        return repo.responseEntityBuild();
    }

    // TODO: Add global Handler
    // public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    // @Override
    // protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
    //     ErrorMessage message = new ErrorMessage(HttpStatus.BAD_REQUEST, "", headers.getLocation().getPath());
    //     return ResponseEntity.status(message.status()).body(message);
    // }
}
