package com.tokyo.expensetracker.service;

import com.tokyo.expensetracker.model.Role;
import com.tokyo.expensetracker.repository.RoleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServices {

    private RoleRepository repository;

    public RoleServices(RoleRepository repository) {
        this.repository = repository;
    }

    public Role findById(int id){
        return repository.findById((byte) id).get();
    }

    public List<Role> saveAll(List<Role> roles) {
         return repository.saveAll(roles);
    }
}
