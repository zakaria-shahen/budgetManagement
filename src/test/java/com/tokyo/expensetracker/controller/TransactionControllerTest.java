package com.tokyo.expensetracker.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tokyo.expensetracker.model.Household;
import com.tokyo.expensetracker.model.Transaction;
import com.tokyo.expensetracker.model.User;
import com.tokyo.expensetracker.service.TransactionService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = TransactionController.class)
class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TransactionService transactionService;

    private final String path = "/api/v1/transactions/";
    private final User user = new User(1L);
    private final Household household = new Household(1L);
    private final Transaction transaction = new Transaction(
            1L,
            BigDecimal.ONE,
            Transaction.Type.DEPOSIT,
            "test",
            user,
            household
    );
    private final List<Transaction> transactionList = List.of(transaction, transaction);


    @Test
    void getAllTransactions() throws Exception {
        when(transactionService.findAll()).thenReturn(List.of());
        mockMvc.perform(get(path).contentType(MediaType.APPLICATION_JSON.toString()))
                .andExpect(status().isOk());

        verify(transactionService, times(1)).findAll();
    }

    @Test
    void getAllByUserID() throws Exception {
        when(transactionService.findAllByUser(1L)).thenReturn(transactionList);

        var responseBody = mockMvc.perform(get(path)
                        .param("user_id", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        verify(transactionService, times(1)).findAllByUser(1L);

        Assertions.assertEquals(
                objectMapper.writeValueAsString(transactionList),
                responseBody
        );
    }

    @Test
    void getById() throws Exception {
        when(transactionService.findById(1L)).thenReturn(transaction);

        var response = mockMvc.perform(get(path + 1).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isFound())
                .andReturn().getResponse().getContentAsString();

        verify(transactionService, times(1)).findById(1L);

        Assertions.assertEquals(
                response,
                objectMapper.writeValueAsString(transaction)
        );

    }

    @Test
    void postTransaction() throws Exception {
        when(transactionService.save(any(Transaction.class))).thenReturn(transaction);

        var response = mockMvc.perform(
                        post(path)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(transaction))
                ).andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        verify(transactionService, times(1)).save(any(Transaction.class));

        Assertions.assertEquals(
                objectMapper.writeValueAsString(transaction),
                response
        );
    }

    @Test
    void deleteTransaction() throws Exception {
        doNothing().when(transactionService).deleteById(1L);

        var response = mockMvc.perform(
                        delete(path + 1).contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                 .andReturn().getResponse().getContentAsString();

        verify(transactionService, times(1)).deleteById(1L);

        Assertions.assertEquals(
                "{\"massage\":\"resource deleted successfully\"}",
                response
        );


    }
}