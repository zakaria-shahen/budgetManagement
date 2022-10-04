package com.myCompany.budgetManagement.repository;

import com.myCompany.budgetManagement.model.Account;
import com.myCompany.budgetManagement.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {

    List<Transaction> findByAccountId(Integer accountId);
}
