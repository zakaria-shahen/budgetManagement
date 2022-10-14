package com.tokyo.expensetracker.service;

import com.tokyo.expensetracker.repository.HouseholdRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.mockito.Mockito.when;

class HouseholdServiceTest {

    @Autowired
    private HouseholdService householdService;

    @MockBean
    private HouseholdRepository householdRepository;

    @Test
    void updateTotalBalance() {
        // TODO: @zakaria-shahen
        // var household = findById(householdId);
        // when(householdRepository.find)

    }
}