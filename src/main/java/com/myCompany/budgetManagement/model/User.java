package com.myCompany.budgetManagement.model;


import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity(name = "user_")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonPropertyOrder({"id", "transactions"})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    @OneToMany(mappedBy = "user")
    private List<Transaction> transactions;

    @ManyToOne
    @JoinColumn(
            name = "household_id",
            referencedColumnName = "id")
    @JsonIncludeProperties("id")
    @JsonUnwrapped(prefix = "household_")
    private Household household;
}

   