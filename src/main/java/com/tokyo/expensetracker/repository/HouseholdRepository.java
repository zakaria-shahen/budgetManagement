package com.tokyo.expensetracker.repository;

import com.tokyo.expensetracker.model.Household;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HouseholdRepository extends JpaRepository<Household, Long> {
    Optional<Household> findByInvitationCode(String invitationCode);
}
