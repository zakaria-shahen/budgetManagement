package com.mycompany.budgetmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mycompany.budgetmanagement.model.User;

public interface AccountRepository extends JpaRepository<User, Integer> {
}
