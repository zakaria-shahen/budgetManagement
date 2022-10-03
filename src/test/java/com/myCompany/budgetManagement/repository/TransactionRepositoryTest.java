package com.myCompany.budgetManagement.repository;

import com.myCompany.budgetManagement.model.Account;
import com.myCompany.budgetManagement.model.Transaction;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TransactionRepositoryTest {

    @Autowired
    private TransactionRepository repository;

    @BeforeEach
    void init() {
        repository.deleteAll();
        float amount = 100.0f;
        repository.save(new Transaction(new Account(), "test 1", amount));
        repository.save(new Transaction(new Account(), "test 2", amount));
        repository.save(new Transaction(new Account(), "test 3", amount));
        repository.save(new Transaction(new Account(), "test 4", amount));
    }

    @Test
    void save() {
        Assertions.assertEquals(repository.findAll().size(), 4);
    }

    @Test
    void delete() {
        var transaction = new Transaction(new Account(), "test 5", 100.00f);
        repository.save(transaction);
        repository.delete(transaction);
        Assertions.assertNull(repository.findById(5));

    }

    @Test
    void findById() {
        Assertions.assertNotNull(repository.findById(2));
        Assertions.assertNull(repository.findById(5));
    }
}