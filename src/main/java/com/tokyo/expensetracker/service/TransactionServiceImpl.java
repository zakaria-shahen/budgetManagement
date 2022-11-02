package com.tokyo.expensetracker.service;

import com.tokyo.expensetracker.exception.InsufficientFundsException;
import com.tokyo.expensetracker.exception.NotFoundException;
import com.tokyo.expensetracker.exception.NotFoundForeignKeyIdException;
import com.tokyo.expensetracker.model.Transaction;
import com.tokyo.expensetracker.repository.TransactionRepository;
import lombok.NoArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {

    private TransactionRepository repository;
    private HouseholdService householdService;

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

        updateTotalBalance(
                transaction.getAmount(),
                transaction.getHousehold().getId(),
                transaction.getType()
        );

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

    protected void updateTotalBalance(BigDecimal amount, Long householdId, Transaction.Type type) {

        var household = householdService.findById(householdId);
        var totalBalance = household.getTotalBalance();

        if (type == Transaction.Type.WITHDRAW) {

            if (0 > totalBalance.compareTo(amount)) {
                throw new InsufficientFundsException("Your Total balance is less then Transaction amount.");
            }

            totalBalance = totalBalance.subtract(amount);

        } else if (type == Transaction.Type.DEPOSIT) {

            totalBalance = totalBalance.add(amount);
        }

        household.setTotalBalance(totalBalance);

    }

}
