package com.daicom.daicombackend.auth.dto;

import java.util.ArrayList;
import java.util.List;

public class AuthResponse {

    private String token;
    private String username;
    private String role;
    // kind = id del rol; 0 = admin
    private Integer kind;
    // permisos del rol (vistas + acciones)
    private List<Integer> action_permissions;

    public AuthResponse(String token, String username, String role) {
        this(token, username, role, null, new ArrayList<>());
    }

    public AuthResponse(String token, String username, String role, Integer kind,
                        List<Integer> action_permissions) {
        this.token = token;
        this.username = username;
        this.role = role;
        this.kind = kind;
        this.action_permissions = action_permissions != null ? action_permissions : new ArrayList<>();
    }

    // Getters
    public String getToken() { return token; }
    public String getUsername() { return username; }
    public String getRole() { return role; }
    public Integer getKind() { return kind; }
    public List<Integer> getAction_permissions() { return action_permissions; }
}
