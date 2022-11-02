package com.tokyo.expensetracker.model;

public enum RoleEnum {
    STAFF(1, "Staff"),
    CO_OWNER(2, "Co-Owner"),
    OWNER(3, "Owner");

    private final int id;
    private final String name;

    RoleEnum(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
