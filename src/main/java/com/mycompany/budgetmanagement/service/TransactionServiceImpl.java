package com.mycompany.budgetmanagement.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.stereotype.Service;

import com.mycompany.budgetmanagement.exception.NotEnteredForeignKeyIdException;
import com.mycompany.budgetmanagement.exception.NotFoundException;
import com.mycompany.budgetmanagement.exception.NotFoundForeignKeyIdException;
import com.mycompany.budgetmanagement.model.Transaction;
import com.mycompany.budgetmanagement.repository.TransactionRepository;

import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {

    TransactionRepository repository;

    @Autowired
    public TransactionServiceImpl(TransactionRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Transaction> findAll() {
        return repository.findAll();
    }

    @Override
    public List<Transaction> findAllByAccount(Integer accountId) {
        return repository.findByAccountId(accountId);
    }

    @Override
    public Transaction findById(Integer id) {
        var transaction = repository.findById(id);
        if (transaction.isEmpty()) {
            throw new NotFoundException("Not Found Transaction");
        }
        return repository.findById(id).get();
    }

    @Override
    public Transaction save(Transaction transaction) {
        try {
            return repository.save(transaction);
        } catch (DataIntegrityViolationException e) {
            // if (e.getCause() instanceof ConstraintViolationException)
           throw new NotFoundForeignKeyIdException("Not Found Account ID (Foreign Key)");
        } catch (InvalidDataAccessApiUsageException e){
            throw new NotEnteredForeignKeyIdException("Must add Account ID (Foreign Key)");

        }
    }

    @Override
    public void deleteAll() {
        repository.deleteAll();
    }

    @Override
    public void deleteById(Integer id) {
        try {
            repository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
             throw new NotFoundException("Not Found Resource (Transaction)");
        }
    }

    @Override
    public Transaction replaceById(Integer id, Transaction transaction) {
        var old = findById(id);
        BeanUtils.copyProperties(transaction, old, "id");
        return old;
    }
}
