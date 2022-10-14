package com.tokyo.expensetracker.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotEmpty;
import java.util.Locale;

@Entity
@Getter
@Setter
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    private String name;

    public Role() {}

    public Role(Long id, String name) {
        this.id = id;
        this.name = name.toLowerCase();
    }

    public Role(String name) {
        this.name = name.toLowerCase();
    }

    @AssertTrue(message = "Must be any of the following values {'staff', 'co-Owner', 'owner'}")
    private boolean isValid(){
        return name.equalsIgnoreCase("Staff")
                || name.equalsIgnoreCase("Co-Owner")
                || name.equalsIgnoreCase("Owner");
    }
}