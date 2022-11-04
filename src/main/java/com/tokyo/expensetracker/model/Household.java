package com.tokyo.expensetracker.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
@SQLDelete(sql = "update household set deleted=true where id = ?")
@Where(clause = "deleted = false")
@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonPropertyOrder({
        "id", "name", "totalBalance", "monthlySpendings",
        "monthlyDeposits", "members",  "transactions",
        "createdAt", "greetingMsg", "invitationCode"
})
public class Household {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty @NotNull
    private String name;

    @PositiveOrZero
    @Builder.Default
    private BigDecimal totalBalance = BigDecimal.ZERO;
    @PositiveOrZero
    private BigDecimal monthlySpendings;
    @PositiveOrZero
    private BigDecimal monthlyDeposits;

    @OneToMany(mappedBy = "household")
    private List<User> members;

    @OneToMany(mappedBy = "household")
    private List<Transaction> transactions;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonFormat(pattern = "dd-mm-yy hh:mm")
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    private String greetingMsg;
    private String invitationCode;

    @JsonIgnore
    @Builder.Default
    private Boolean deleted = false;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Household household = (Household) o;
        return Objects.equals(id, household.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
