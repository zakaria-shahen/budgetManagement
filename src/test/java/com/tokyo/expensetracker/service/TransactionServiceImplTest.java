package com.tokyo.expensetracker.service;

import com.tokyo.expensetracker.exception.NotFoundException;
import com.tokyo.expensetracker.exception.NotFoundForeignKeyIdException;
import com.tokyo.expensetracker.model.Household;
import com.tokyo.expensetracker.model.Transaction;
import com.tokyo.expensetracker.model.User;
import com.tokyo.expensetracker.repository.TransactionRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.EmptyResultDataAccessException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@SpringBootTest
class TransactionServiceImplTest {

    @Autowired
    private TransactionService transactionService;

    @MockBean
    private TransactionRepository transactionRepository;

    @MockBean
    private HouseholdService householdService;

    private final Transaction transaction = new Transaction(
            1L,
            BigDecimal.ONE,
            Transaction.Type.DEPOSIT,
            "test",
            new User(1L),
            new Household(1L)
    );

    private final List<Transaction> transactionList = List.of(transaction, transaction);

    @Test
    void findById(){
        when(transactionRepository.findById(1L)).thenReturn(Optional.of(transaction));

        Transaction output = transactionService.findById(1L);

        verify(transactionRepository, times(1)).findById(1L);

        Assertions.assertEquals(transaction, output);
    }


    @Test
    void findByIdWithException(){
        when(transactionRepository.findById(1L)).thenReturn(Optional.empty());

        Assertions.assertThrows(NotFoundException.class, () -> transactionService.findById(1L));

        verify(transactionRepository, times(1)).findById(1L);

    }

    @Test
    void save() {
        doNothing().when(householdService).updateTotalBalance(
             transaction.getAmount(),
             transaction.getHousehold().getId(),
             transaction.getType()
        );
        when(transactionRepository.save(transaction)).thenReturn(transaction);


        Assertions.assertEquals(transactionService.save(transaction), transaction);

        verify(householdService, times(1)).updateTotalBalance(           transaction.getAmount(),
                transaction.getHousehold().getId(),
                transaction.getType()
        );

        verify(transactionRepository, times(1)).save(transaction);


    }
    

    @Test
    void saveWithNotFoundForeignKeyIdException() {
        doNothing().when(householdService).updateTotalBalance(
                transaction.getAmount(),
                transaction.getHousehold().getId(),
                transaction.getType()
        );

        doThrow(NotFoundForeignKeyIdException.class)
                .when(transactionRepository).save(transaction);


        Assertions.assertThrows(
                NotFoundForeignKeyIdException.class,
                () -> transactionService.save(transaction)
        );

        verify(householdService, times(1)).updateTotalBalance(           transaction.getAmount(),
                transaction.getHousehold().getId(),
                transaction.getType()
        );

        verify(transactionRepository, times(1)).save(transaction);

    }

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
}