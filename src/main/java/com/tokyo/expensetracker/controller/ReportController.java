package com.tokyo.expensetracker.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tokyo.expensetracker.model.Transaction;
import com.tokyo.expensetracker.service.ReportService;

@RestController
@RequestMapping("/api/v1/reports")
public class ReportController {
    private ReportService service;

    public ReportController(ReportService service) {
        this.service = service;
    }

    // TODO: add authentication
    @GetMapping
    public ResponseEntity<List<Transaction>> get(
            @RequestParam(name = "user_id") Long userId,
            @RequestParam(name = "household_id") Long householdId) {

        if (userId != null) {
            return new ResponseEntity<>(service.findAllByUserId(userId), HttpStatus.OK);
        }

        if (householdId != null) {
            return new ResponseEntity<>(service.findAllByHouseholdId(householdId), HttpStatus.OK);
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

}
