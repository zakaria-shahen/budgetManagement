package com.myCompany.budgetManagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.myCompany.budgetManagement.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
