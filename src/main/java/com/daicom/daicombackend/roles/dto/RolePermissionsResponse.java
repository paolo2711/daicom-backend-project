package com.daicom.daicombackend.roles.dto;

import com.daicom.daicombackend.roles.RolePermission;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Respuesta de GET /role_permissions/{roleId}: { permission_list: [PermissionItem...] }.
 */
public class RolePermissionsResponse {

    private List<PermissionItem> permission_list;

    public RolePermissionsResponse(List<RolePermission> grants) {
        this.permission_list = grants.stream()
                .map(g -> new PermissionItem(g.getPermissionCode(), g.getName(), g.getEndpoint()))
                .collect(Collectors.toList());
    }

    public List<PermissionItem> getPermission_list() { return permission_list; }
}
