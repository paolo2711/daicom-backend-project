package com.daicom.daicombackend.roles;

import jakarta.persistence.*;

/**
 * Permiso concedido a un rol. El catálogo de permisos lo define el frontend
 * (vistas del sidebar + acciones del sistema), por lo que aquí se persiste
 * directamente el código numérico junto con su nombre y endpoint legibles.
 */
@Entity
@Table(name = "role_permissions")
public class RolePermission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    // codigo: vista (1,2,5...) o accion (10-14)
    @Column(nullable = false)
    private Integer permissionCode;

    @Column(nullable = false)
    private String name;

    private String endpoint;

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }
    public Integer getPermissionCode() { return permissionCode; }
    public void setPermissionCode(Integer permissionCode) { this.permissionCode = permissionCode; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEndpoint() { return endpoint; }
    public void setEndpoint(String endpoint) { this.endpoint = endpoint; }
}
