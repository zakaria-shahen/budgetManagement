package com.tokyo.expensetracker.model;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
@NoArgsConstructor
@JsonPropertyOrder({"id", "name"})
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Byte id;

    @NotEmpty
    private String name;

    protected Role(int id, String name) {
        this.id = (byte) id;
        this.name = name;
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