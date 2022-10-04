package com.myCompany.budgetManagement.service;

import com.myCompany.budgetManagement.model.Transaction;

import java.util.List;
import java.util.Optional;

public interface TransactionService {
    List<Transaction> findAll();

    List<Transaction> findAllByAccount(Integer accountId);

    Transaction findById(Integer id);

    Transaction save(Transaction transaction);

    void deleteAll();

    void deleteById(Integer id);

    Transaction replaceById(Integer id, Transaction transaction);
}
