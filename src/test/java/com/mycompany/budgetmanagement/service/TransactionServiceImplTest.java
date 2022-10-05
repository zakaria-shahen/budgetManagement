package com.mycompany.budgetmanagement.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.mycompany.budgetmanagement.service.TransactionService;

class TransactionServiceImplTest {

    private TransactionService transactionService;

    @Autowired
    public void setTransactionService(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @BeforeEach
    void init() {

    }

    @Test
    void findAll() {
    }

    @Test
    void findAllByAccount() {
    }

    @Test
    void findById() {
    }

    @Test
    void save() {
    }

    @Test
    void deleteAll() {
    }

    @Test
    void deleteById() {
    }

    @Test
    void replaceById() {
    }
}