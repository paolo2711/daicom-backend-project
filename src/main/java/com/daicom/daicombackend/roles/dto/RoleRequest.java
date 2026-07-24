package com.daicom.daicombackend.roles.dto;

import jakarta.validation.constraints.NotBlank;

public class RoleRequest {

    @NotBlank(message = "El nombre del rol es obligatorio")
    private String name;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}
