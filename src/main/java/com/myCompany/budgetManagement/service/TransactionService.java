package com.myCompany.budgetManagement.service;

import com.myCompany.budgetManagement.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {

    @Autowired
    TransactionRepository repository;


}
