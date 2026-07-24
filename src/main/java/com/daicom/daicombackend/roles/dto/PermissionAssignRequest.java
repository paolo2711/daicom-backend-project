package com.daicom.daicombackend.roles.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Payload de POST /permissions: role (id del rol) y permission_list (lista de permisos).
 * Reemplaza por completo los permisos concedidos al rol indicado.
 */
public class PermissionAssignRequest {

    private Long role;
    private List<PermissionItem> permission_list = new ArrayList<>();

    public Long getRole() { return role; }
    public void setRole(Long role) { this.role = role; }
    public List<PermissionItem> getPermission_list() { return permission_list; }
    public void setPermission_list(List<PermissionItem> permission_list) { this.permission_list = permission_list; }
}
