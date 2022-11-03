package com.tokyo.expensetracker.repository;

import com.tokyo.expensetracker.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface RoleRepository extends JpaRepository<Role, Byte> {
    Role findByName(String name);
}
