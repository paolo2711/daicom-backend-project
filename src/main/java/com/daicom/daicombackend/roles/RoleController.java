package com.daicom.daicombackend.roles;

import com.daicom.daicombackend.roles.dto.PermissionAssignRequest;
import com.daicom.daicombackend.roles.dto.RolePermissionsResponse;
import com.daicom.daicombackend.roles.dto.RoleRequest;
import com.daicom.daicombackend.roles.dto.RoleResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * API de Roles y Permisos. Las rutas de permisos (/role_permissions, /permissions)
 * viven aquí, junto al dominio de roles, con paths explícitos por método.
 */
@RestController
public class RoleController {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    // --- ROLES ---
    @GetMapping("/roles")
    public ResponseEntity<List<RoleResponse>> getAll() {
        return ResponseEntity.ok(roleService.findAll());
    }

    @GetMapping("/roles/{id}")
    public ResponseEntity<RoleResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(roleService.findById(id));
    }

    @PostMapping("/roles")
    public ResponseEntity<RoleResponse> create(@Valid @RequestBody RoleRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(roleService.create(request));
    }

    @PutMapping("/roles/{id}")
    public ResponseEntity<RoleResponse> update(@PathVariable Long id, @Valid @RequestBody RoleRequest request) {
        return ResponseEntity.ok(roleService.update(id, request));
    }

    @DeleteMapping("/roles/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        roleService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // --- PERMISOS ---
    @GetMapping("/role_permissions/{roleId}")
    public ResponseEntity<RolePermissionsResponse> getPermissions(@PathVariable Long roleId) {
        return ResponseEntity.ok(roleService.getPermissions(roleId));
    }

    @PostMapping("/permissions")
    public ResponseEntity<Void> assignPermissions(@RequestBody PermissionAssignRequest request) {
        roleService.assignPermissions(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
