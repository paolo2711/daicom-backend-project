package com.daicom.daicombackend.roles.dto;

import com.daicom.daicombackend.roles.Role;

public class RoleResponse {

    private Long id;
    private String name;
    private boolean admin;

    public RoleResponse(Role role) {
        this.id = role.getId();
        this.name = role.getName();
        this.admin = role.isAdmin();
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public boolean isAdmin() { return admin; }
}
