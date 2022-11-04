package com.tokyo.expensetracker.model;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;
import java.util.Objects;

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
        this.name = name;
    }

    public Role(int id) {
        this.id = (byte) id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

