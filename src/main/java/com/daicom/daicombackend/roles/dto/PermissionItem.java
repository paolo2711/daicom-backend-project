package com.daicom.daicombackend.roles.dto;

/**
 * Un permiso dentro de permission_list, tal cual lo envía y espera el frontend:
 * { permission_id, name, endpoint }.
 */
public class PermissionItem {

    private Integer permission_id;
    private String name;
    private String endpoint;

    public PermissionItem() {}

    public PermissionItem(Integer permission_id, String name, String endpoint) {
        this.permission_id = permission_id;
        this.name = name;
        this.endpoint = endpoint;
    }

    public Integer getPermission_id() { return permission_id; }
    public void setPermission_id(Integer permission_id) { this.permission_id = permission_id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEndpoint() { return endpoint; }
    public void setEndpoint(String endpoint) { this.endpoint = endpoint; }
}
