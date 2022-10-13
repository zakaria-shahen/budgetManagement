package com.tokyo.expensetracker.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
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
    @Min(1)
    private List<User> members;

    @OneToMany(mappedBy = "household")
    private List<Transaction> transactions;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonFormat(pattern = "dd-mm-yy hh:mm")
    private LocalDateTime createdAt = LocalDateTime.now();

    private String greetingMsg;
    private String invitationCode = name;
}
