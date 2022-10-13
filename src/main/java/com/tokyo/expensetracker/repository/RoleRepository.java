package com.tokyo.expensetracker.repository;

import org.springframework.stereotype.Repository;

import com.tokyo.expensetracker.model.Role;

import java.util.HashMap;
import java.util.Map;


@Repository
public class RoleRepository {

    private Map<Integer, Role> roles = new HashMap<>();

    public RoleRepository() {
        roles.put(1,new Role(1,"Owner") ) ;
        roles.put(2,new Role(2,"Co-Owner") ) ;
        roles.put(3,new Role(3,"Staff") ) ;
    }

    public Role getRole(short id){
        return roles.get(id);
    }
}