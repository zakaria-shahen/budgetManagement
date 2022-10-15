package com.tokyo.expensetracker.service;

import com.tokyo.expensetracker.exception.InsufficientFundsException;
import com.tokyo.expensetracker.exception.NotFoundException;
import com.tokyo.expensetracker.exception.NotFoundForeignKeyIdException;
import com.tokyo.expensetracker.model.Household;
import com.tokyo.expensetracker.model.Transaction;
import com.tokyo.expensetracker.model.User;
import com.tokyo.expensetracker.repository.TransactionRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@SpringBootTest
class TransactionServiceImplTest {

    @MockBean
    private TransactionRepository transactionRepository;

    @MockBean
    private HouseholdService householdService;

    @Autowired
    private TransactionServiceImpl transactionService;
    private final Transaction transaction = new Transaction(
            1L,
            BigDecimal.ONE,
            Transaction.Type.DEPOSIT,
            "test",
            new User(1L),
            new Household(1L, BigDecimal.valueOf(100))
    );
    private final List<Transaction> transactionList = List.of(transaction, transaction);
    private final Household household = transaction.getHousehold();


    @Test
    void findById() {
        when(transactionRepository.findById(1L)).thenReturn(Optional.of(transaction));

        Transaction output = transactionService.findById(1L);

        verify(transactionRepository, times(1)).findById(1L);

        Assertions.assertEquals(transaction, output);
    }

    @Test
    void findByIdWithException() {
        when(transactionRepository.findById(1L)).thenReturn(Optional.empty());

        Assertions.assertThrows(NotFoundException.class, () -> transactionService.findById(1L));

        verify(transactionRepository, times(1)).findById(1L);

    }

    @Test
    void findAll() {
        when(transactionRepository.findAll()).thenReturn(transactionList);

        Assertions.assertEquals(transactionService.findAll(), transactionList);

        verify(transactionRepository, times(1)).findAll();

    }

    @Test
    void findAllByUser() {
        when(transactionRepository.findByUserId(0L)).thenReturn(transactionList);

        Assertions.assertEquals(transactionService.findAllByUser(0L), transactionList);

        verify(transactionRepository, times(1)).findByUserId(0L);

    }

    @Test
    void findAllByHousehold() {
        when(transactionRepository.findByHouseholdId(0L)).thenReturn(transactionList);

        Assertions.assertEquals(transactionService.findAllByHousehold(0L), transactionList);

        verify(transactionRepository, times(1)).findByHouseholdId(0L);

    }

    @Nested
    class save {

        @Test
        void saveMethod() {

            var spyTransactionServiceImpl = spy(new TransactionServiceImpl(transactionRepository, householdService));

            doNothing().when(spyTransactionServiceImpl).updateTotalBalance(
                    transaction.getAmount(),
                    household.getId(),
                    transaction.getType()
            );

            when(transactionRepository.save(transaction)).thenReturn(transaction);
            when(householdService.findById(household.getId()))
                    .thenReturn(household);

            Assertions.assertEquals(spyTransactionServiceImpl.save(transaction), transaction);

            verify(spyTransactionServiceImpl, times(1)).updateTotalBalance(
                    transaction.getAmount(),
                    household.getId(),
                    transaction.getType()
            );

            verify(transactionRepository, times(1)).save(transaction);


        }

        @Test
        void saveWithNotFoundForeignKeyIdException() {
            var spy = spy(new TransactionServiceImpl(transactionRepository, householdService));

            doThrow(DataIntegrityViolationException.class)
                    .when(transactionRepository).save(transaction);

            doNothing().when(spy).updateTotalBalance(
                    transaction.getAmount(),
                    household.getId(),
                    transaction.getType()
            );

            Assertions.assertThrows(
                    NotFoundForeignKeyIdException.class,
                    () -> spy.save(transaction)
            );

            verify(spy, times(1)).save(transaction);

            verify(spy, times(1)).updateTotalBalance(
                    transaction.getAmount(),
                    household.getId(),
                    transaction.getType()
            );
        }

    }

    @Nested
    class deleteBy {
        @Test
        void deleteById() {
            doNothing().when(transactionRepository).deleteById(0L);

            transactionService.deleteById(0L);

            verify(transactionRepository, times(1)).deleteById(0L);

        }

        @Test
        void deleteByIdWithException() {
            doThrow(EmptyResultDataAccessException.class).when(transactionRepository).deleteById(0L);

            Assertions.assertThrows(NotFoundException.class, () -> transactionService.deleteById(0L));

            verify(transactionRepository, times(1)).deleteById(0L);

        }
    }

    @Nested
    class updateTotalBalance {


        @Test
        void updateTotalBalanceTypeDeposit() {
            var totalBalance = household.getTotalBalance();
            transaction.setType(Transaction.Type.DEPOSIT);

            when(householdService.findById(1L)).thenReturn(household);

            transactionService.updateTotalBalance(
                    transaction.getAmount(),
                    household.getId(),
                    transaction.getType()
            );

            verify(householdService, times(1))
                    .findById(1L);

            Assertions.assertEquals(
                    transaction.getAmount().add(totalBalance),
                    household.getTotalBalance()
            );

        }


        @Test
        void updateTotalBalanceTypeDepositTypeWithdraw() {
            transaction.setType(Transaction.Type.WITHDRAW);
            var totalBalance = household.getTotalBalance();

            when(householdService.findById(1L)).thenReturn(household);

            transactionService.updateTotalBalance(
                    transaction.getAmount(),
                    household.getId(),
                    transaction.getType()
            );

            verify(householdService, times(1)).findById(1L);

            Assertions.assertEquals(
                    totalBalance.subtract(transaction.getAmount()),
                    household.getTotalBalance()
            );
        }

        @Test
        void updateTotalBalanceTypeDepositTypeWithdrawAndThrowInsufficientFundsException() {
            var totalBalance = household.getTotalBalance();
            transaction.setType(Transaction.Type.WITHDRAW);
            transaction.setAmount(totalBalance.add(BigDecimal.ONE));

            when(householdService.findById(1L)).thenReturn(household);

            Assertions.assertThrows(InsufficientFundsException.class, () ->
                    transactionService.updateTotalBalance(
                            transaction.getAmount(),
                            household.getId(),
                            transaction.getType()
                    ));

            verify(householdService, times(1)).findById(1L);

            Assertions.assertEquals(
                    totalBalance,
                    household.getTotalBalance()
            );

        }


    }


}