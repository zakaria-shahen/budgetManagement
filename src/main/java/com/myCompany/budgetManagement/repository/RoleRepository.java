package com.myCompany.budgetManagement.repository;

import com.myCompany.budgetManagement.model.Role;
import org.springframework.stereotype.Repository;

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
