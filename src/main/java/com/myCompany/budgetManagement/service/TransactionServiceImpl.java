package com.myCompany.budgetManagement.service;

import com.myCompany.budgetManagement.exception.NotEnteredForeignKeyIdException;
import com.myCompany.budgetManagement.exception.NotFoundException;
import com.myCompany.budgetManagement.exception.NotFoundForeignKeyIdException;
import com.myCompany.budgetManagement.model.Transaction;
import com.myCompany.budgetManagement.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.stereotype.Service;

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
           throw new NotFoundForeignKeyIdException("Not Found User/household ID (Foreign Key)");
        } catch (InvalidDataAccessApiUsageException e){
            throw new NotEnteredForeignKeyIdException("Must add User/household ID (Foreign Key)");

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
