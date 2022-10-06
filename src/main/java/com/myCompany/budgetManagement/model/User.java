package com.myCompany.budgetManagement.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;

import java.util.List;

@Entity(name = "user_")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "user")
    private List<Transaction> transactions;

    @ManyToOne
    @JoinColumn(
            name = "household_id",
            referencedColumnName = "id")
    @JsonIncludeProperties("id")
    @NotNull
    private Household household;
}
