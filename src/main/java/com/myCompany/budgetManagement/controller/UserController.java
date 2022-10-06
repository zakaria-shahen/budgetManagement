package com.myCompany.budgetManagement.controller;

import com.myCompany.budgetManagement.model.User;
import com.myCompany.budgetManagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/users")
    public ResponseEntity<?> getAll(){
        return  ResponseEntity.status(HttpStatus.OK).body(userService.getAllUser());
    }

    @PostMapping("/user")
    public ResponseEntity<?> save(@RequestBody User user){
        try{
            return ResponseEntity.status(HttpStatus.CREATED).body(userService.saveUser(user));
        }catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("BAD REQUEST");
        }

    }

    @GetMapping("/user/{id}")
    public ResponseEntity<?> getUser(@PathVariable Long id){
        try {
            return ResponseEntity.status(HttpStatus.OK).body(userService.findUserById(id));
        }catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("NO such User");
        }

    }

    @PutMapping("/user")
    public ResponseEntity<?> updateUser(@RequestBody User user){
        try {
            return ResponseEntity.status(HttpStatus.OK).body(userService.update(user));
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad Request");
        }

    }

    @DeleteMapping("/user/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable long id){
        try {
            userService.deleteUser(id);
            return ResponseEntity.status(HttpStatus.OK).body("UserDeleted");
        }catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("NO Such User");
        }

    }
}
