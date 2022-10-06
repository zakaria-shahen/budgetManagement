package com.myCompany.budgetManagement.exception;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.springframework.http.HttpStatus;

@JsonPropertyOrder({"status", "message", "path"})
public record ErrorMessage(
        HttpStatus status,
        String message,
        String path
) {
}
