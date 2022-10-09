package com.myCompany.budgetManagement.exception;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@JsonPropertyOrder({"status", "message", "path"})
public record ErrorMessage(
        HttpStatus status,
        String message,
        String path
) {
    public ResponseEntity<Object> BuildResponseEntity(){
        return ResponseEntity.status(status).body(this);
    }
}
