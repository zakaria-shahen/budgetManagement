package com.myCompany.budgetManagement.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.net.ConnectException;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@ControllerAdvice(basePackages = "com.myCompany.budgetManagement")
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, Object> NotFoundHandler(HttpServletRequest request, Exception e) {
        return responseBuilder(HttpStatus.NOT_FOUND.value(), request.getRequestURI(), e.getMessage());
    }

    @ExceptionHandler(NotFoundForeignKeyIdException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Map<String, Object> NotFoundForeignKeyIdHandle(HttpServletRequest request, Exception e){
        return responseBuilder(HttpStatus.CONFLICT.value(), request.getRequestURI(), e.getMessage());
    }

    @ExceptionHandler(NotEnteredForeignKeyIdException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> NotEnteredForeignKeyIdHandle(HttpServletRequest request, Exception e){
        return responseBuilder(HttpStatus.BAD_REQUEST.value(), request.getRequestURI(), e.getMessage());
    }


    @ExceptionHandler(ConnectException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, Object> DatabaseConnectHandler(HttpServletRequest request) {
        return responseBuilder(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                request.getRequestURI(),
                "DB Connection refused");
    }

    private Map<String, Object> responseBuilder(int status, String url, String massage) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", status);
        response.put("massage", massage);
        response.put("path", url);
        return response;
    }


}
