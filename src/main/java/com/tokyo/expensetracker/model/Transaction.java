package com.tokyo.expensetracker.model;

import com.fasterxml.jackson.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({"id", "amount", "memo", "date", "user", "household"})
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @NotNull
    private Type type;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(
            name = "user_id",
            referencedColumnName = "id",
            nullable = false)
    @JsonIncludeProperties("id")
    @JsonUnwrapped(prefix = "user_")
    @NotNull
    private User user;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(
            name = "household_id",
            referencedColumnName = "id",
            nullable = false)
    @JsonIncludeProperties("id")
    @JsonUnwrapped(prefix = "household_")
    @NotNull
    private Household household;

    @NotEmpty
    private String memo;

    @Min(1)
    private BigDecimal amount;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonFormat(pattern = "dd-mm-yyyy hh:mm")
    private LocalDateTime date = LocalDateTime.now();


    public enum Type {
        WITHDRAW,
        DEPOSIT,
    }
}
