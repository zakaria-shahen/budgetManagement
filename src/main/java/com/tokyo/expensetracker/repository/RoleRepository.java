package com.tokyo.expensetracker.repository;

import org.springframework.stereotype.Repository;

import com.tokyo.expensetracker.model.Role;

import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    
}
