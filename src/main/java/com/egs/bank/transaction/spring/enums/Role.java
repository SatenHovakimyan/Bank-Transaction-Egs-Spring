package com.egs.bank.transaction.spring.enums;

import java.util.List;

public enum Role {
    USER(1),
    ADMIN(2);

    private final int id;

    Role(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static Role getById(int id) {
        List<Role> roles = List.of(Role.values());
        for (Role role:
             roles) {
            if(role.id == id) {
                return role;
            }
        }
        return null;
    }
}
