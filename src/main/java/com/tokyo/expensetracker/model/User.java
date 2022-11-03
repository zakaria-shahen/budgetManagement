package com.tokyo.expensetracker.model;


import com.fasterxml.jackson.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity(name = "user_")
@SQLDelete(sql = "update user_ set deleted=true where id = ?")
@Where(clause = "deleted = false")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonPropertyOrder({"id", "name", "role", "household", "transactions"})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty @NotNull
    private String name;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", referencedColumnName = "id", nullable = false)
    @JsonIncludeProperties("id")
    @JsonUnwrapped(prefix = "role_")
    private Role role;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(
            name = "household_id",
            referencedColumnName = "id",
            nullable = false)
    @JsonIncludeProperties("id")
    @JsonUnwrapped(prefix = "household_")
    private Household household;

    @OneToMany(mappedBy = "user")
    private List<Transaction> transactions;

    @AssertTrue(message = "Must add User ID (Foreign Key)")
    @JsonIgnore
    public boolean isValidHouseholdId(){
        return household.getId() != null;
    }

    @AssertTrue(message = "Must add Role ID (Foreign Key)")
    @JsonIgnore
    public boolean isValidRoleId(){
        return role.getId() != null;
    }

    @JsonIgnore
    private Boolean deleted = false;

    public User(long id) {
        this.id = id;
    }

}