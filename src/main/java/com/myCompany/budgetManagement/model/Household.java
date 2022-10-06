package com.myCompany.budgetManagement.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Household {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    String name;
    String invitationCode;
    String greetingMsg;

    @OneToMany(mappedBy = "household")
    private List<User> members;

    @OneToMany(mappedBy = "household")
    private List<Transaction> transactions;

    BigDecimal totalBalance;
    BigDecimal monthlySpendings;
    BigDecimal monthlyDeposits;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime createdAt = LocalDateTime.now();

}
