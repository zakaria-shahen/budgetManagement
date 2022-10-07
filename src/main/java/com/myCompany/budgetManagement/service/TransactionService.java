package com.myCompany.budgetManagement.service;

import java.util.List;

import com.myCompany.budgetManagement.model.Transaction;

public interface TransactionService {
    List<Transaction> findAll();

    List<Transaction> findAllByUser(Long accountId);

    List<Transaction> findAllByHousehold(Long householdId);

    Transaction findById(Long id);

    Transaction save(Transaction transaction);

    void deleteAll();

    void deleteById(Long id);

}
