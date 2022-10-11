package com.tokyo.expensetracker.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.stereotype.Service;

import com.tokyo.expensetracker.exception.NotEnteredForeignKeyIdException;
import com.tokyo.expensetracker.exception.NotFoundException;
import com.tokyo.expensetracker.exception.NotFoundForeignKeyIdException;
import com.tokyo.expensetracker.model.Transaction;
import com.tokyo.expensetracker.repository.TransactionRepository;

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
    public List<Transaction> findAllByUser(Long UserId) {
        return repository.findByUserId(UserId);
    }

    @Override
    public List<Transaction> findAllByHousehold(Long householdId) {
        return repository.findByHouseholdId(householdId);
    }

    @Override
    public Transaction findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Not Found Transaction"));
    }

    @Override
    public Transaction save(Transaction transaction) {
        try {
            return repository.save(transaction);
        } catch (DataIntegrityViolationException e) {
            // if (e.getCause() instanceof ConstraintViolationException)
           throw new NotFoundForeignKeyIdException("Not Found User ID or/and household ID (Foreign Key)");
        } catch (InvalidDataAccessApiUsageException e){
            throw new NotEnteredForeignKeyIdException("Must add User ID or/and household ID (Foreign Key)");

        }
    }

    @Override
    public void deleteAll() {
        repository.deleteAll();
    }

    @Override
    public void deleteById(Long id) {
        try {
            repository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
             throw new NotFoundException("Not Found Resource (Transaction)");
        }
    }
}