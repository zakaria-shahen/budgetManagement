package com.tokyo.expensetracker.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.transaction.CannotCreateTransactionException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.net.ConnectException;

@ControllerAdvice(basePackages = "com.myCompany.budgetManagement")
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMissingPathVariable
            (MissingPathVariableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

        return new ErrorMessage(HttpStatus.BAD_REQUEST,
                "Missing Path Variable",
                getRequestUri(request))
                .BuildResponseEntity();
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid
            (MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

        return new ArgumentNotValidResponseBuilder(
                getRequestUri(request),
                HttpStatus.BAD_REQUEST,
                ex
        ).BuildResponseEntity();
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return new ErrorMessage(
                HttpStatus.BAD_REQUEST,
                "JSON parse error",
                getRequestUri(request)
        ).BuildResponseEntity();
    }

    private static String getRequestUri(WebRequest request) {
        return getRequestUri(((ServletWebRequest) request).getRequest());
    }

    private static String getRequestUri(HttpServletRequest request) {
        return request.getRequestURI();
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<?> NotFoundHandler(HttpServletRequest request, Exception e) {
        return new ErrorMessage(
                HttpStatus.NOT_FOUND,
                e.getMessage(),
                getRequestUri(request)
        ).BuildResponseEntity();
    }

    @ExceptionHandler({NotFoundForeignKeyIdException.class, DeleteDataIntegrityViolationException.class})
    public ResponseEntity<?> conflictHandler(HttpServletRequest request, Exception e) {
        return new ErrorMessage(
                HttpStatus.CONFLICT,
                e.getMessage(),
                getRequestUri(request)
        ).BuildResponseEntity();
    }

    @ExceptionHandler({NotEnteredForeignKeyIdException.class, NotEnteredAllRequiredFieldException.class})
    public ResponseEntity<?> badRequestHandler(HttpServletRequest request, Exception e) {
        return new ErrorMessage(
                HttpStatus.BAD_REQUEST,
                e.getMessage(),
                getRequestUri(request)
        ).BuildResponseEntity();
    }

    @ExceptionHandler({ConnectException.class, CannotCreateTransactionException.class})
    public ResponseEntity<?> DatabaseConnectHandler(HttpServletRequest request) {
        return new ErrorMessage(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "DB Connection refused",
                getRequestUri(request)
        ).BuildResponseEntity();
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleOther(HttpServletRequest request, Exception e) {
        return new ErrorMessage(
                HttpStatus.BAD_REQUEST,
                "Unspecified request problem",
                getRequestUri(request)
        ).BuildResponseEntity();
    }

}
