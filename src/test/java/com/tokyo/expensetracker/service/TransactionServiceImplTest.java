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
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import static org.mockito.BDDMockito.*;

@ExtendWith(SpringExtension.class)
class TransactionServiceImplTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private HouseholdService householdService;

    @InjectMocks
    private TransactionServiceImpl transactionService;
    private final Transaction transaction = Transaction.builder()
            .id(1L)
            .amount(BigDecimal.ONE)
            .type(Transaction.Type.DEPOSIT)
            .memo("test")
            .user(User.builder().id(1L).build())
            .household(Household.builder().id(1L).totalBalance(BigDecimal.ONE).build())
            .build();
    private final List<Transaction> transactionList = List.of(transaction, transaction);
    private final Household household = transaction.getHousehold();


    @Test
    void findById() {
        given(transactionRepository.findById(1L)).willReturn(Optional.of(transaction));

        Transaction output = transactionService.findById(1L);
        
        then(transactionRepository).should().findById(1L);

        Assertions.assertEquals(transaction, output);
    }

    @Test
    void findByIdWithException() {
        given(transactionRepository.findById(1L)).willReturn(Optional.empty());

        Assertions.assertThrows(NotFoundException.class, () -> transactionService.findById(1L));

        then(transactionRepository).should().findById(1L);

    }

    @Test
    void findAll() {
        given(transactionRepository.findAll()).willReturn(transactionList);

        Assertions.assertEquals(transactionService.findAll(), transactionList);

        then(transactionRepository).should().findAll();

    }

    @Test
    void findAllByUser() {
        given(transactionRepository.findByUserId(0L)).willReturn(transactionList);

        Assertions.assertEquals(transactionService.findAllByUser(0L), transactionList);

        then(transactionRepository).should().findByUserId(0L);

    }

    @Test
    void findAllByHousehold() {
        given(transactionRepository.findByHouseholdId(0L)).willReturn(transactionList);

        Assertions.assertEquals(transactionService.findAllByHousehold(0L), transactionList);

        then(transactionRepository).should().findByHouseholdId(0L);

    }

    @Nested
    class save {

        private final TransactionServiceImpl spyTransactionServiceImpl = spy(transactionService);

        @Test
        void saveMethod() {

             willDoNothing().given(spyTransactionServiceImpl).updateTotalBalance(
                    transaction.getAmount(),
                    household.getId(),
                    transaction.getType()
            );

            given(transactionRepository.save(transaction)).willReturn(transaction);
            given(householdService.findById(household.getId()))
                    .willReturn(household);

            Assertions.assertEquals(spyTransactionServiceImpl.save(transaction), transaction);

            then(spyTransactionServiceImpl).should().updateTotalBalance(
                    transaction.getAmount(),
                    household.getId(),
                    transaction.getType()
            );

            then(transactionRepository).should().save(transaction);


        }

        @Test
        void saveWithNotFoundForeignKeyIdException() {

            willThrow(DataIntegrityViolationException.class)
                    .given(transactionRepository).save(transaction);

            willDoNothing().given(spyTransactionServiceImpl).updateTotalBalance(
                    transaction.getAmount(),
                    household.getId(),
                    transaction.getType()
            );

            Assertions.assertThrows(
                    NotFoundForeignKeyIdException.class,
                    () -> spyTransactionServiceImpl.save(transaction)
            );

            then(spyTransactionServiceImpl).should().save(transaction);

            then(spyTransactionServiceImpl).should().updateTotalBalance(
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
            willDoNothing().given(transactionRepository).deleteById(0L);

            transactionService.deleteById(0L);

            then(transactionRepository).should().deleteById(0L);

        }

        @Test
        void deleteByIdWithException() {
            willThrow(EmptyResultDataAccessException.class)
                    .given(transactionRepository).deleteById(0L);

            Assertions.assertThrows(NotFoundException.class, () -> transactionService.deleteById(0L));

            then(transactionRepository).should().deleteById(0L);

        }
    }

    @Nested
    class updateTotalBalance {

        private final Consumer<Transaction> updateTotalBalance = t -> transactionService.updateTotalBalance(
                t.getAmount(),
                t.getHousehold().getId(),
                t.getType()
        );

        @Test
        void updateTotalBalanceTypeDeposit() {
            var totalBalance = household.getTotalBalance();
            transaction.setType(Transaction.Type.DEPOSIT);

            given(householdService.findById(1L)).willReturn(household);

            updateTotalBalance.accept(transaction);

            then(householdService).should()
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

            given(householdService.findById(1L)).willReturn(household);

            updateTotalBalance.accept(transaction);

            then(householdService).should().findById(1L);

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

            given(householdService.findById(1L)).willReturn(household);

            Assertions.assertThrows(InsufficientFundsException.class, () ->
                    updateTotalBalance.accept(transaction)
            );

            then(householdService).should().findById(1L);

            Assertions.assertEquals(
                    totalBalance,
                    household.getTotalBalance()
            );

        }



    }


}