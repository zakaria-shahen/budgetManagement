package com.myCompany.budgetManagement.repository;

import com.myCompany.budgetManagement.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Integer> {
}
