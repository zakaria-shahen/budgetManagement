package com.myCompany.budgetManagement.service;

import com.myCompany.budgetManagement.model.Role;
import com.myCompany.budgetManagement.model.User;
import com.myCompany.budgetManagement.repository.RoleRepository;
import com.myCompany.budgetManagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    public List<User> getAllUser(){
        return  userRepository.findAll();
    }

    public User findUserById(int id ){
        return userRepository.findById(id);
    }

    public User deleteUser(int id){
        User user = findUserById(id);
        if (user == null)  throw new RuntimeException();
      return    userRepository.delete(id);
    }

    public User saveUser(User user){

        Role role =  roleRepository.getRole(user.getRole().getId());
        if(role != null) {
            user.setRole(role);
            return userRepository.save(user);
        }  else {
            throw new RuntimeException();
        }
    }

    public User update(User user){
      return   userRepository.update(user);
    }
}
