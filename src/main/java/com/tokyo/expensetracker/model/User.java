package com.tokyo.expensetracker.model;


import com.fasterxml.jackson.annotation.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;

@Entity(name = "user_")
@SQLDelete(sql = "update user_ set deleted=true where id = ?")
@Where(clause = "deleted = false")
@Builder
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
    @Column(unique = true)
    private String name;

    @NotEmpty @NotNull
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
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

    @JsonIgnore
    @Builder.Default
    private Boolean deleted = false;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}