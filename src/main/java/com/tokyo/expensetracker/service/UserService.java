package com.tokyo.expensetracker.service;

import com.tokyo.expensetracker.exception.NotFoundException;
import com.tokyo.expensetracker.exception.NotFoundForeignKeyIdException;
import com.tokyo.expensetracker.model.Household;
import com.tokyo.expensetracker.model.User;
import com.tokyo.expensetracker.repository.UserRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.List;

@Service
public class UserService {

    private final UserRepository repository;
    private final HouseholdService householdService;

    public UserService(UserRepository repository, @Lazy HouseholdService householdService) {
        this.repository = repository;
        this.householdService = householdService;
    }

    public List<User> getAllUser(){
        return  repository.findAll();
    }

    public User findUserById(Long id ){
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found with id = " + id));
    }

    public void deleteUser(Long id){
        User user = findUserById(id);
        Household household = user.getHousehold();
        int userRoleId = user.getRole().getId();

        if(userRoleId == 1 && repository.countByHouseholdIdAndRoleId(household.getId(), (byte) 1) == 1) {
            repository.delete(user);
            householdService.delete(household);
        } else {
            repository.delete(user);
        }


    }

    public User saveUser(User user){
        if (householdService.findById(user.getHousehold().getId()) == null) {
            throw new NotFoundForeignKeyIdException("Not Found household ID (foreign key)");
        }

        try {
            return repository.save(user);

        } catch (DataIntegrityViolationException e){
            throw new NotFoundForeignKeyIdException("Not Found role (foreign key)");
        }
    }

    public User update(User user){
        return saveUser(user);
    }

    protected void setHouseholdForUserId(User user, @NotNull Household household){
        user.setHousehold(household);
        repository.save(user);
    }
}
