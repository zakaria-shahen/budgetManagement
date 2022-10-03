package com.myCompany.budgetManagement.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import java.time.LocalDateTime;
import java.util.Objects;

public class Transaction {
    private int id;
    @JsonBackReference
    final private Account account;
    final private String memo;
    final private float amount;
    final private LocalDateTime date;

    public Transaction(Account account, String memo, float amount) {
        this.account = Objects.requireNonNull(account);
        this.amount = amount;
        this.date = LocalDateTime.now();
        this.memo = Objects.requireNonNull(memo);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Account getAccount() {
        return account;
    }

    public String getMemo() {
        return memo;
    }

    public float getAmount() {
        return amount;
    }

    public LocalDateTime getDate() {
        return date;
    }
}
