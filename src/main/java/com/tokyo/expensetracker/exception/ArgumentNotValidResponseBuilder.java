package com.tokyo.expensetracker.exception;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@JsonPropertyOrder({"status", "message", "errors", "path"})
public class ArgumentNotValidResponseBuilder {

    private final HttpStatus status;
    private final String message;
    private final Map<String, String> errors;
    private final String path;

    public ArgumentNotValidResponseBuilder(String path, HttpStatus status, MethodArgumentNotValidException e) {
        this.path = path;
        this.status = status;
        this.errors = errorsBuilder(e.getFieldErrors());
        this.message = messageBuilder(e.getObjectName(), e.getErrorCount());
    }

    public ResponseEntity<Object> BuildResponseEntity() {
        return new ResponseEntity<>(this, status);
    }

    private static String messageBuilder(String objectName, int ErrorCount) {
        return "Validation failed for object='%s'. Error count: %s".formatted(objectName, ErrorCount);
    }

    private static Map<String, String> errorsBuilder(List<FieldError> e) {
        var errorMap = new HashMap<String, String>();
        e.forEach(x -> errorMap.put(x.getField(), x.getDefaultMessage()));

        return errorMap;
    }
}
