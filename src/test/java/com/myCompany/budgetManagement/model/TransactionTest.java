package com.myCompany.budgetManagement.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
class TransactionTest {

    @Test
    public void constructorTest(){
        var memo = "test";
        var account = new Account();
        float amount = 100.0f;
        Transaction transaction = new Transaction(account, memo, amount);

        Assertions.assertEquals(transaction.getAccount(), account);
        Assertions.assertEquals(transaction.getMemo(), memo);
        Assertions.assertEquals(transaction.getAmount(), amount);
    }

}