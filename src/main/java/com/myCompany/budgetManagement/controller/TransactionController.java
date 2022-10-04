package com.myCompany.budgetManagement.controller;


import com.myCompany.budgetManagement.model.Transaction;
import com.myCompany.budgetManagement.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/transactions")
public class TransactionController {

    @Autowired
    private TransactionRepository repository;

    @GetMapping
    public List<Transaction> getByAccount(@RequestParam(required = false) Integer id){
        if (id == null){
            return repository.findAll();
        }
        return repository.findAllByAccount(id);
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getByID(@PathVariable Integer id){
        Transaction transaction = repository.findById(id);
        if (transaction == null){
            return new ResponseEntity<Map<String, String>>(Map.of("massage", "not found"), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Transaction>(transaction, HttpStatus.FOUND);

    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Transaction post(@RequestBody Transaction transaction){
        return repository.save(transaction);
    }

    @DeleteMapping
    public Map<String, String> delete(){
        repository.deleteAll();
        return Map.of("massage", "resource deleted successfully");
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Map<String, String>> deleteByID(@PathVariable Integer id){
        if (repository.findById(id) == null){
            return new ResponseEntity<>(Map.of("massage", "Not Found"), HttpStatus.NOT_FOUND);
        }
        repository.deleteId(id);
        return new ResponseEntity<>(Map.of("massage", "resource updated successfully"), HttpStatus.OK);
    }

    @PutMapping("{id}")
    public Map<String, String> put(@PathVariable Integer id, @RequestBody Transaction transaction){
        if (repository.findById(id) == null){
            repository.save(transaction);
        } else {
            repository.replace(id, transaction);
        }

        return Map.of("massage", "resource updated successfully");
    }

}
