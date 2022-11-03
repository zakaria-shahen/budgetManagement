package com.tokyo.expensetracker.service;

import com.tokyo.expensetracker.exception.DeleteDataIntegrityViolationException;
import com.tokyo.expensetracker.exception.NotEnteredForeignKeyIdException;
import com.tokyo.expensetracker.exception.NotFoundException;
import com.tokyo.expensetracker.exception.UserNotMemberOfYourHouseholdOrHouseholdNotExists;
import com.tokyo.expensetracker.model.Household;
import com.tokyo.expensetracker.model.User;
import com.tokyo.expensetracker.repository.HouseholdRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ConstraintViolationException;
import java.math.BigDecimal;
import java.util.List;
import java.util.function.LongFunction;

@Service
public class HouseholdService {

    private final HouseholdRepository householdRepository;
    private final UserService userService;
    private static final LongFunction<String> householdNotFoundMessage = id -> "Household not found with id = " + id;

    public HouseholdService(HouseholdRepository householdRepository, UserService userService) {
        this.householdRepository = householdRepository;
        this.userService = userService;
    }

    public List<Household> findAll() {
        return householdRepository.findAll();
    }

    public Household findById(Long id) {
        return householdRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException(householdNotFoundMessage.apply(id)));
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

    public Household update(Long id, Household newHousehold) {

        Household household = findById(id);
        BeanUtils.copyProperties(newHousehold, household, "id", "members", "transactions");

        try {
            return householdRepository.save(household);
        } catch (InvalidDataAccessApiUsageException | DataIntegrityViolationException
                 | TransactionSystemException e){
            throw new NotEnteredForeignKeyIdException("body request should have: {name, totalBalance}");
        }
    }

    public void deleteById(Long id) {
        try {
            householdRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException(householdNotFoundMessage.apply(id));
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

    // TODO: admin level
    @Transactional
    public void deleteMember(Long householdId, Long memberId) {
        User user = userService.findUserById(memberId);
        if (! user.getHousehold().getId().equals(householdId)){
             throw new UserNotMemberOfYourHouseholdOrHouseholdNotExists(householdId, memberId);
        }

        Household household = new Household();
        household.setName("Personal");
        // TODO: change to user.getBalance()
        household.setTotalBalance(BigDecimal.ZERO);
        householdRepository.save(household);

        // TODO: user->setRole to owner

        userService.setHouseholdForUserId(user, household);
    }

}
