package com.myCompany.budgetManagement.service;

import com.myCompany.budgetManagement.exception.NotFoundException;
import com.myCompany.budgetManagement.exception.NotFoundForeignKeyIdException;
import com.myCompany.budgetManagement.model.Transaction;
import com.myCompany.budgetManagement.repository.TransactionRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
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
