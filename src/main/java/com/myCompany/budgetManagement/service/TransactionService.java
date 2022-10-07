package com.myCompany.budgetManagement.service;

import com.myCompany.budgetManagement.model.Transaction;

import java.util.List;

public interface TransactionService {
    List<Transaction> findAll();

    List<Transaction> findAllByUser(Long accountId);

    List<Transaction> findAllByHousehold(Long householdId);

    Transaction findById(Long id);

    Transaction save(Transaction transaction);

    void deleteAll();

    void deleteById(Long id);

}
