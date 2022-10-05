package com.mycompany.budgetmanagement.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.mycompany.budgetmanagement.exception.NotFoundException;
import com.mycompany.budgetmanagement.model.Transaction;
import com.mycompany.budgetmanagement.service.TransactionService;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/transactions")
public class TransactionController {

    private TransactionService service;

    @Autowired
    public TransactionController(TransactionService service) {
        this.service = service;
    }

    @GetMapping
    public List<Transaction> getByAccount(
            @RequestParam(required = false, name = "account_id") Integer accountId) {
        if (accountId == null) {
            return service.findAll();
        }
        return service.findAllByAccount(accountId);
    }

    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.FOUND)
    public Transaction getByID(@PathVariable Integer id) {
        return service.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Object post(@Valid @RequestBody Transaction transaction) {
        return service.save(transaction);
    }

    @DeleteMapping
    public Map<String, String> delete() {
        service.deleteAll();
        return Map.of("massage", "resource deleted successfully");
    }

    @DeleteMapping("{id}")
    public Map<String, String> deleteByID(@PathVariable Integer id) {
        service.deleteById(id);
        return Map.of("massage", "resource updated successfully");
    }

    @PutMapping("{id}")
    public Transaction put(@PathVariable Integer id, @RequestBody Transaction transaction) {
        try {
            return service.replaceById(id, transaction);
        } catch (NotFoundException e) {
            return service.save(transaction);
        }
    }

}
