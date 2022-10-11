package com.tokyo.expensetracker.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.tokyo.expensetracker.model.Transaction;

@Service
public class ReportService {

    private TransactionService transactionService;

    public ReportService(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    public List<Transaction> findAllByUserId(Long userId) {
        return transactionService.findAllByUser(userId);
    }

    public List<Transaction> findAllByHouseholdId(Long householdId) {
        return transactionService.findAllByHousehold(householdId);
    }

}