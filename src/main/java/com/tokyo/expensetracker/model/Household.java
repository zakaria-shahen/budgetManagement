package com.tokyo.expensetracker.model;

import com.fasterxml.jackson.annotation.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@SQLDelete(sql = "update household set deleted=true where id = ?")
@Where(clause = "deleted = false")
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
    private BigDecimal totalBalance;
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
    private LocalDateTime createdAt = LocalDateTime.now();

    private String greetingMsg;
    private String invitationCode;

    @JsonIgnore
    private Boolean deleted = false;

    public Household(Long id) {
        this.id = id;
    }

    public Household(Long id, BigDecimal totalBalance) {
        this.id = id;
        this.totalBalance = totalBalance;
    }
}
