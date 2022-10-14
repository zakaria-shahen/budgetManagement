package com.tokyo.expensetracker.service;

import com.tokyo.expensetracker.exception.InsufficientFundsException;
import com.tokyo.expensetracker.model.Transaction;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.stereotype.Service;

import com.tokyo.expensetracker.exception.DeleteDataIntegrityViolationException;
import com.tokyo.expensetracker.exception.NotEnteredForeignKeyIdException;
import com.tokyo.expensetracker.exception.NotFoundException;
import com.tokyo.expensetracker.model.Household;
import com.tokyo.expensetracker.model.User;
import com.tokyo.expensetracker.repository.HouseholdRepository;
import com.tokyo.expensetracker.repository.UserRepository;

import java.math.BigDecimal;
import java.util.List;

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

    public Household create(Household household) {
        try {
           return householdRepository.save(household);
        } catch (InvalidDataAccessApiUsageException | DataIntegrityViolationException e){
            throw new NotEnteredForeignKeyIdException("body request should have: {name, totalBalance}");
        }
    }

    public Household update(Long id, Household household) {
        if (!householdRepository.existsById(id)) {
            throw new NotFoundException("Household not found with id = " + id);
        }

        household.setId(id);
        try {
            return householdRepository.save(household);
        } catch (InvalidDataAccessApiUsageException | DataIntegrityViolationException e){
            throw new NotEnteredForeignKeyIdException("body request should have: {name, totalBalance}");
        }
    }

    public void deleteById(Long id) {
        try {
            householdRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Household not found with id = " + id);
        } catch (DataIntegrityViolationException e) {
            // TODO: household and FK
            throw new DeleteDataIntegrityViolationException("Cannot delete household: " +
                    "Must Delete all Transactions or/and the all Members leaves the household");
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


    protected void updateTotalBalance(BigDecimal amount, Long householdId, Transaction.Type type) {

        var household = findById(householdId);
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
