package com.myCompany.budgetManagement.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.myCompany.budgetManagement.model.Household;

@Repository
public interface HouseholdRepository extends JpaRepository<Household, Long> {
    Optional<Household> findByInvitationCode(String invitationCode);
}
