package com.myCompany.budgetManagement.exception;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Collection;
import java.util.LinkedHashMap;
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

    private String messageBuilder(String objectName, int ErrorCount) {
        return "Validation failed for object='%s'. Error count: %s".formatted(objectName, ErrorCount);
    }

    public ResponseEntity<ArgumentNotValidResponseBuilder> responseEntityBuild() {
        return new ResponseEntity<>(this, status);
    }

    private LinkedHashMap<String, String> errorsBuilder(List<FieldError> e) {
        var errorsArrayString = e.stream()
                .map(x -> List.of(x.getField(), x.getDefaultMessage()))
                .flatMap(Collection::stream)
                .toArray(String[]::new);

        var errorMap = new LinkedHashMap<String, String>();

        for (int i = 0; i < errorsArrayString.length / 2; i++) {
            errorMap.put(errorsArrayString[i], errorsArrayString[++i]);
        }

        return errorMap;
    }
}
