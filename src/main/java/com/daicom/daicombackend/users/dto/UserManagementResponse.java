package com.daicom.daicombackend.users.dto;

import com.daicom.daicombackend.auth.User;

/**
 * Representación de usuario para la pantalla de Usuarios.
 * `kind` = id del rol asignado, `kind_description` = nombre del rol.
 */
public class UserManagementResponse {

    private Long id;
    private String username;
    private String first_name;
    private String last_name;
    private String email;
    private Long kind;
    private String kind_description;

    public UserManagementResponse(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.first_name = user.getFirstName();
        this.last_name = user.getLastName();
        this.email = user.getEmail();
        if (user.getRoleEntity() != null) {
            this.kind = user.getRoleEntity().getId();
            this.kind_description = user.getRoleEntity().getName();
        } else {
            // Usuario sin rol granular asignado (ej. sembrado original)
            this.kind = null;
            this.kind_description = user.getRole() != null ? user.getRole().name() : "Sin rol";
        }
    }

    public Long getId() { return id; }
    public String getUsername() { return username; }
    public String getFirst_name() { return first_name; }
    public String getLast_name() { return last_name; }
    public String getEmail() { return email; }
    public Long getKind() { return kind; }
    public String getKind_description() { return kind_description; }
}
