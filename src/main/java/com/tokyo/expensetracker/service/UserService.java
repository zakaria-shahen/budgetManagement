package com.tokyo.expensetracker.service;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.stereotype.Service;

import com.tokyo.expensetracker.exception.*;
import com.tokyo.expensetracker.model.User;
import com.tokyo.expensetracker.repository.UserRepository;

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
        }  catch (DataIntegrityViolationException e) {
            // TODO: User and FK
            throw new DeleteDataIntegrityViolationException("Cannot delete User: " +
                            "Must Delete all user Transactions or/and the user leaves the household");
        }
    }

    public User saveUser(User user){
        try {
            return repository.save(user);
        } catch (InvalidDataAccessApiUsageException e){
            throw new NotEnteredForeignKeyIdException("body request should have: {id, name, role_id, household_id}");

        } catch (DataIntegrityViolationException e){
            throw new NotFoundForeignKeyIdException("Not Found household ID or/and role (foreign key)");

        }
    }

    public User update(User user){
        try {
            return repository.save(user);

        } catch (InvalidDataAccessApiUsageException e) {
            throw new NotEnteredAllRequiredFieldException("body request should have: {id, name, role_id, household_id}");

        } catch (DataIntegrityViolationException e){
            throw new NotFoundForeignKeyIdException("Not Found household ID or/and role (foreign key)");
        }
    }
}
