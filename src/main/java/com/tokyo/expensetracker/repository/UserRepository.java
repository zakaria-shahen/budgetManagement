package com.tokyo.expensetracker.repository;

import com.tokyo.expensetracker.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    Long countByHouseholdIdAndRoleId(Long householdId, Byte roleId);
}
