package com.myCompany.budgetManagement.service;

import com.myCompany.budgetManagement.model.Role;
import com.myCompany.budgetManagement.model.User;
import com.myCompany.budgetManagement.repository.RoleRepository;
import com.myCompany.budgetManagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    public List<User> getAllUser(){
        return  userRepository.findAll();
    }

    public User findUserById(Long id ){
        Optional<User> optionalUser = userRepository.findById(id);
        return optionalUser.get();
    }

    public void deleteUser(Long id){
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isEmpty())  throw new RuntimeException();
        userRepository.delete(userOptional.get());
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
      return   userRepository.save(user);
    }
}
