package com.daicom.daicombackend.users.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * Payload de creación/edición de usuario desde la pantalla de Usuarios.
 * `kind` es el id del rol asignado (roles.Role).
 */
public class UserManagementRequest {

    @NotBlank(message = "El usuario es obligatorio")
    private String username;

    private String first_name;
    private String last_name;
    private String email;
    private String password;

    // Id del rol asignado (roles.Role)
    private Long kind;

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getFirst_name() { return first_name; }
    public void setFirst_name(String first_name) { this.first_name = first_name; }
    public String getLast_name() { return last_name; }
    public void setLast_name(String last_name) { this.last_name = last_name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public Long getKind() { return kind; }
    public void setKind(Long kind) { this.kind = kind; }
}
