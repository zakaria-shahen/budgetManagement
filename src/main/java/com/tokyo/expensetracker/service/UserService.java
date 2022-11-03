package com.tokyo.expensetracker.service;

import com.tokyo.expensetracker.exception.NotFoundException;
import com.tokyo.expensetracker.exception.NotFoundForeignKeyIdException;
import com.tokyo.expensetracker.model.Household;
import com.tokyo.expensetracker.model.User;
import com.tokyo.expensetracker.repository.UserRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.List;

@Service
public class UserService {

    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public List<User> getAllUser(){
        return  repository.findAll();
    }

    public User findUserById(Long id ){
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found with id = " + id));
    }

    public void deleteUser(Long id){
        try {
            repository.deleteById(id);
        } catch (EmptyResultDataAccessException e){
            throw new NotFoundException("Not Found User");
        }
    }

    public User saveUser(User user){
        try {
            return repository.save(user);

        } catch (DataIntegrityViolationException e){
            throw new NotFoundForeignKeyIdException("Not Found household ID or/and role (foreign key)");
        }
    }

    public User update(User user){
        try {
            return repository.save(user);

        } catch (DataIntegrityViolationException e){
            throw new NotFoundForeignKeyIdException("Not Found household ID or/and role (foreign key)");
        }
    }

    protected void setHouseholdForUserId(User user, @NotNull Household household){
        user.setHousehold(household);
        repository.save(user);
    }
}
