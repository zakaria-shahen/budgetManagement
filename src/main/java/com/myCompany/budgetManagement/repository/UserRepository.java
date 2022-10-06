package com.myCompany.budgetManagement.repository;

import com.myCompany.budgetManagement.model.User;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class UserRepository {

    private HashMap<Integer,User> users = new HashMap<>();
    private int count = 0;

    public List<User> findAll(){
       return new ArrayList<>(users.values());
    }

    public User findById(int id){
        return users.get(id);
    }

    public User save(User user){
        user.setId(++count);
        users.put(user.getId(), user);

        return user;
    }

    public User update(User user){
        users.put(user.getId(), user);
        return user;
    }

    public User delete(int id){
        return users.remove(id);
    }

}
