package com.tokyo.expensetracker.exception;

public class UserNotMemberOfYourHouseholdOrHouseholdNotExists extends RuntimeException {
    public UserNotMemberOfYourHouseholdOrHouseholdNotExists(Long householdId, Long memberId) {
        super(
            "User=" + memberId + " is not a member of your household=" + householdId
            + " or Household not exists"
        );
    }
}
