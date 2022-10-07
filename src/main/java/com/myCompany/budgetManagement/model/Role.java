package com.myCompany.budgetManagement.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotEmpty;

@Entity
@Getter
@Setter
@AllArgsConstructor
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotEmpty
    private String name;

    public Role() {}

    @AssertTrue(message = "Must be any of the following values {'staff', 'co-Owner', 'owner'}")
    private boolean isValid(){
         return name.equalsIgnoreCase("Staff")
                 || name.equalsIgnoreCase("Co-Owner")
                 || name.equalsIgnoreCase("Owner");
    }
}

