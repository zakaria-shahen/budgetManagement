package com.myCompany.budgetManagement.controller;


import com.myCompany.budgetManagement.model.Transaction;
import com.myCompany.budgetManagement.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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
    public List<Transaction> get() {
            return service.findAll();
    }

    @GetMapping(params = "user_id")
    public List<Transaction> getByUser(@RequestParam(name = "user_id") Long UserId) {
        return service.findAllByUser(UserId);
    }

    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.FOUND)
    public Transaction getByID(@PathVariable Long id) {
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
    public Map<String, String> deleteByID(@PathVariable Long id) {
        service.deleteById(id);
        return Map.of("massage", "resources deleted successfully");
    }

}
