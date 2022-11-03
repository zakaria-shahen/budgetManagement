package com.tokyo.expensetracker.service;

import com.tokyo.expensetracker.exception.NotEnteredForeignKeyIdException;
import com.tokyo.expensetracker.exception.NotFoundException;
import com.tokyo.expensetracker.exception.UserNotMemberOfYourHouseholdOrHouseholdNotExists;
import com.tokyo.expensetracker.model.Household;
import com.tokyo.expensetracker.model.User;
import com.tokyo.expensetracker.repository.HouseholdRepository;
import com.tokyo.expensetracker.repository.RoleRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.function.LongFunction;

@Service
public class HouseholdService {

    private final HouseholdRepository householdRepository;
    private final UserService userService;
    private final RoleServices roleServices;
    private static final LongFunction<String> householdNotFoundMessage = id -> "Household not found with id = " + id;

    public HouseholdService(HouseholdRepository householdRepository, UserService userService, RoleServices roleServices) {
        this.householdRepository = householdRepository;
        this.userService = userService;
        this.roleServices = roleServices;
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
        Household household = findById(id);
        delete(household);
    }

    public List<User> findAllMembers(Long householdId) {
        Household household = findById(householdId);
        return household.getMembers();
    }

    // TODO: admin level
    @Transactional
    public void deleteMember(Long householdId, Long memberId) {
        User member = userService.findUserById(memberId);
        if (! member.getHousehold().getId().equals(householdId)){
             throw new UserNotMemberOfYourHouseholdOrHouseholdNotExists(householdId, memberId);
        }

        switchMemberToNewHousehold(member);
    }

    protected void delete(Household household) {
        if (household.getMembers() != null) {
            household.getMembers()
                    .forEach(this::switchMemberToNewHousehold);
        }

        householdRepository.delete(household);
    }

    private void switchMemberToNewHousehold(User member) {
        Household household = Household.builder()
                .name("Personal")
        // TODO: change to member.getBalance()
                .totalBalance(BigDecimal.ZERO).build();
        householdRepository.save(household);


        member.setRole(roleServices.findById(1));

        userService.setHouseholdForUserId(member, household);
    }

}
