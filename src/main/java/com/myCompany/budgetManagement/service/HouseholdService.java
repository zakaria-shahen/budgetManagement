package com.myCompany.budgetManagement.service;

import java.util.List;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.myCompany.budgetManagement.exception.NotFoundException;
import com.myCompany.budgetManagement.model.Household;
import com.myCompany.budgetManagement.repository.HouseholdRepository;

@Service
public class HouseholdService {

    private HouseholdRepository householdRepository;

    public HouseholdService(HouseholdRepository householdRepository) {
        this.householdRepository = householdRepository;
    }

    public List<Household> findAll() {
        return householdRepository.findAll();
    }

    public Household findById(Long id) {
        return householdRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException("Household not found with id = " + id));
    }

    public Household findByInviationCode(String invitationCode) {
        return householdRepository
                .findByInvitationCode(invitationCode)
                .orElseThrow(() -> new NotFoundException("Household not found with code = " + invitationCode));
    }

    public void create(Household household) {
        householdRepository.save(household);
    }

    public void update(Long id, Household household) {
        if (!householdRepository.existsById(id)) {
            throw new NotFoundException("Household not found with id = " + id);
        }

        household.setId(id);
        householdRepository.save(household);
    }

    public void deleteById(Long id) {
        try {
            householdRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
             throw new NotFoundException("Household not found with id = " + id);
        }
    }

}
