package com.myCompany.budgetManagement.service;

import java.util.List;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.myCompany.budgetManagement.exception.NotFoundException;
import com.myCompany.budgetManagement.model.Household;
import com.myCompany.budgetManagement.model.User;
import com.myCompany.budgetManagement.repository.HouseholdRepository;
import com.myCompany.budgetManagement.repository.UserRepository;

@Service
public class HouseholdService {

    private HouseholdRepository householdRepository;
    private UserRepository userRepository;

    public HouseholdService(HouseholdRepository householdRepository, UserRepository userRepository) {
        this.householdRepository = householdRepository;
        this.userRepository = userRepository;
    }

    public List<Household> findAll() {
        return householdRepository.findAll();
    }

    public Household findById(Long id) {
        return householdRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException("Household not found with id = " + id));
    }

    public Household findByInvitationCode(String invitationCode) {
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

    // ---------------------------------------------------------------------------

    public List<User> findAllMembers(Long householdId) {
        Household household = findById(householdId);
        return household.getMembers();
    }

    // TODO: use UserService instead
    public void addMember(Long householdId, Long memberId) {
        Household household = findById(householdId);
        User member = userRepository
                .findById(memberId)
                .orElseThrow(() -> new NotFoundException("User not found with id = " + memberId));

        member.setHousehold(household);
        household.getMembers().add(member);

        householdRepository.save(household);
        userRepository.save(member);
    }

    // TODO: use UserService instead
    public void deleteMember(Long householdId, Long memberId) {
        Household household = findById(householdId);
        User member = userRepository
                .findById(memberId)
                .orElseThrow(() -> new NotFoundException("User not found with id = " + memberId));

        member.setHousehold(null);
        household.getMembers().remove(member);

        householdRepository.save(household);
        userRepository.save(member);
    }

}
