package com.tokyo.expensetracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tokyo.expensetracker.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
