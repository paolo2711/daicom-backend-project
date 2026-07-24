package com.daicom.daicombackend.roles;

import com.daicom.daicombackend.company.Company;
import com.daicom.daicombackend.company.CompanyRepository;
import com.daicom.daicombackend.roles.dto.PermissionAssignRequest;
import com.daicom.daicombackend.roles.dto.PermissionItem;
import com.daicom.daicombackend.roles.dto.RolePermissionsResponse;
import com.daicom.daicombackend.roles.dto.RoleRequest;
import com.daicom.daicombackend.roles.dto.RoleResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoleService {

    private final RoleRepository roleRepository;
    private final RolePermissionRepository rolePermissionRepository;
    private final CompanyRepository companyRepository;

    public RoleService(RoleRepository roleRepository, RolePermissionRepository rolePermissionRepository,
                       CompanyRepository companyRepository) {
        this.roleRepository = roleRepository;
        this.rolePermissionRepository = rolePermissionRepository;
        this.companyRepository = companyRepository;
    }

    private Company getMainCompany() {
        return companyRepository.findAll().stream().findFirst()
                .orElseThrow(() -> new RuntimeException("Error: No existe compañía configurada."));
    }

    // --- CRUD DE ROLES ---
    public List<RoleResponse> findAll() {
        return roleRepository.findAll().stream()
                .map(RoleResponse::new)
                .collect(Collectors.toList());
    }

    public RoleResponse findById(Long id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado."));
        return new RoleResponse(role);
    }

    public RoleResponse create(RoleRequest request) {
        Role role = new Role();
        role.setName(request.getName());
        role.setAdmin(false);
        role.setCompany(getMainCompany());
        return new RoleResponse(roleRepository.save(role));
    }

    public RoleResponse update(Long id, RoleRequest request) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado."));
        if (role.isAdmin()) {
            throw new RuntimeException("El rol de administrador no se puede editar.");
        }
        role.setName(request.getName());
        return new RoleResponse(roleRepository.save(role));
    }

    @Transactional
    public void delete(Long id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado."));
        if (role.isAdmin()) {
            throw new RuntimeException("El rol de administrador no se puede eliminar.");
        }
        rolePermissionRepository.deleteByRoleId(id);
        roleRepository.delete(role);
    }

    // --- PERMISOS DE UN ROL ---
    public RolePermissionsResponse getPermissions(Long roleId) {
        return new RolePermissionsResponse(rolePermissionRepository.findByRoleId(roleId));
    }

    /**
     * Reemplaza por completo el set de permisos concedidos a un rol.
     */
    @Transactional
    public void assignPermissions(PermissionAssignRequest request) {
        Role role = roleRepository.findById(request.getRole())
                .orElseThrow(() -> new RuntimeException("Rol no encontrado."));

        rolePermissionRepository.deleteByRoleId(role.getId());

        for (PermissionItem item : request.getPermission_list()) {
            if (item.getPermission_id() == null) continue;
            RolePermission grant = new RolePermission();
            grant.setRole(role);
            grant.setPermissionCode(item.getPermission_id());
            grant.setName(item.getName() != null ? item.getName() : "");
            grant.setEndpoint(item.getEndpoint());
            rolePermissionRepository.save(grant);
        }
    }
}
