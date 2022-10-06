package com.myCompany.budgetManagement.model;

import lombok.Data;

@Data
public class User {

    private int id;

    private String name;

    private Role role;
}
