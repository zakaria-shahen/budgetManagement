package com.tokyo.expensetracker.service;

import com.tokyo.expensetracker.exception.InsufficientFundsException;
import com.tokyo.expensetracker.exception.NotFoundException;
import com.tokyo.expensetracker.exception.NotFoundForeignKeyIdException;
import com.tokyo.expensetracker.model.Transaction;
import com.tokyo.expensetracker.repository.TransactionRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {

    TransactionRepository repository;
    HouseholdService householdService;

    public TransactionServiceImpl(TransactionRepository repository, HouseholdService householdService) {
        this.repository = repository;
        this.householdService = householdService;
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

        updateTotalBalance(transaction);

        try {
            return repository.save(transaction);

        } catch (DataIntegrityViolationException e) {
           throw new NotFoundForeignKeyIdException("Not Found User ID (Foreign Key)");
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


    private void updateTotalBalance(Transaction transaction) {

        var household = householdService.findById(transaction.getHousehold().getId());
        var totalBalance = household.getTotalBalance();

        if (transaction.getType() == Transaction.Type.WITHDRAW) {

            if (0 > totalBalance.compareTo(transaction.getAmount())) {
                throw new InsufficientFundsException("Your Total balance is less then Transaction amount.");
            }

            totalBalance = totalBalance.subtract(transaction.getAmount());

        } else if (transaction.getType() == Transaction.Type.DEPOSIT) {

            totalBalance = totalBalance.add(transaction.getAmount());
        }

        household.setTotalBalance(totalBalance);

    }

}
